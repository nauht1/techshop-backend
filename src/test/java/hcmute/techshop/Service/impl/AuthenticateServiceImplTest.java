package hcmute.techshop.Service.impl;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Model.Auth.AuthRequest;
import hcmute.techshop.Model.Auth.AuthResponse;
import hcmute.techshop.Model.Auth.RegisterDTO;
import hcmute.techshop.Model.Auth.RegisterResponse;
import hcmute.techshop.Repository.Auth.TokenRepository;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.Auth.AuthenticateServiceImpl;
import hcmute.techshop.Service.Auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private AuthenticateServiceImpl authenticateService;

    @BeforeEach
    void setUp() {
    }

    /**
     * Test trường hợp đăng ký thành công
     */
    @Test
    void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO("test@gmail.com", "password123", "testUser");

        when(userRepository.findByEmail(registerDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse response = authenticateService.register(registerDTO);

        assertEquals("Successfully registered", response.getMessage());
        assertEquals(registerDTO.getEmail(), response.getEmail());
        assertEquals(registerDTO.getUsername(), response.getUsername());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();

        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(registerDTO.getEmail(), savedUser.getEmail());
    }

    /**
     * Test trường hợp đăng ký thất bại vì email đã tồn tại
     */
    @Test
    void testRegister_EmailAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("test@gmail.com", "password123", "testUser");

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail(registerDTO.getEmail());

        when(userRepository.findByEmail(registerDTO.getEmail()))
                .thenReturn(Optional.of(existingUser)); // Trung email

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticateService.register(registerDTO);
        });

        assertEquals("Email already in use", exception.getMessage());
    }

    /**
     * Test đăng nhập thành công
     */
    @Test
    void testAuthenticate_Success() {
        AuthRequest authRequest = new AuthRequest("test@gmail.com", "password123");
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .password("encodedPassword")
                .username("testUser")
                .isActive(true)
                .role(Role.ROLE_CUSTOMER)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("mockedJwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("mockedRefreshToken");

        AuthResponse response = authenticateService.authenticate(authRequest);

        assertNotNull(response);
        assertEquals("mockedJwtToken", response.getAccessToken());
        assertEquals("mockedRefreshToken", response.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository, times(1)).save(any());
    }

    /**
     * Test đăng nhập thất bại khi tài khoản chưa được kích hoạt
     */
    @Test
    void testAuthenticate_UserNotVerified() {
        AuthRequest authRequest = new AuthRequest("test@gmail.com", "password123");
        UserEntity user = UserEntity.builder()
                .email("test@gmail.com")
                .password("encodedPassword")
                .username("testUser")
                .isActive(false) // Chưa kích hoạt
                .role(Role.ROLE_CUSTOMER)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            authenticateService.authenticate(authRequest);
        });

        assertEquals("User is not verified", exception.getMessage());
    }
}
