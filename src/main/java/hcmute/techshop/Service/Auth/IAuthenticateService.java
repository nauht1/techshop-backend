package hcmute.techshop.Service.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Auth.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IAuthenticateService {
    RegisterResponse register(RegisterDTO registerDTO);
    AuthResponse authenticate(AuthRequest authRequest);
    void saveUserToken(UserEntity user, String jwtToken);
    void revokeAllUserTokens(UserEntity user);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    VerifyResponse VerifiedCode(String email, String code);
    ForgotPasswordResponse forgotPassword(String email);
}
