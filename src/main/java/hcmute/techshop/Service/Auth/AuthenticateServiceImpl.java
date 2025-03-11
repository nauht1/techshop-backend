package hcmute.techshop.Service.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.techshop.Entity.Auth.TokenEntity;
import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Enum.TokenType;
import hcmute.techshop.Model.Auth.AuthRequest;
import hcmute.techshop.Model.Auth.AuthResponse;
import hcmute.techshop.Model.Auth.RegisterDTO;
import hcmute.techshop.Model.Auth.RegisterResponse;
import hcmute.techshop.Repository.Auth.TokenRepository;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.Email.EmailServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements IAuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final EmailServiceImpl emailService;

    @Override
    public RegisterResponse register(RegisterDTO registerDTO) {
        // Random String
        String verificationCode = RandomString.make(6);

        // Find if user has registered yet ?
        Optional<UserEntity> existUser = userRepository.findByEmail(registerDTO.getEmail());
        if (existUser.isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Save user
        var user = UserEntity.builder()
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .username(registerDTO.getUsername())
                .isActive(false)
                .code(verificationCode)
                .role(Role.ROLE_CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .checkCode(false)
                .build();

        emailService.sendMailRegister(registerDTO.getEmail(), verificationCode);
        var savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .message("Successfully registered")
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail().trim(),
                        authRequest.getPassword().trim()
                )
        );
        // Find if the user is existed?
        var user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Check if the user is enabled?
        if (!user.isActive()) {
            throw new IllegalStateException("User is not verified");
        }

        // Generate JWT and refresh tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void saveUserToken(UserEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public void VerifiedCode(String email, String code) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot found user in system!!!"));

        if(user.getCode().equals(code)) {
            user.setCheckCode(true);
            this.userRepository.save(user);
        } else {
            throw new RuntimeException("Your code provided does not match");
        }
    }
}
