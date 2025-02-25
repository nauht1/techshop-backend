package hcmute.techshop.Service;

import hcmute.techshop.Entity.UserEntity;
import hcmute.techshop.Model.Authentication.AuthRequest;
import hcmute.techshop.Model.Authentication.AuthResponse;
import hcmute.techshop.Model.Authentication.RegisterDTO;
import hcmute.techshop.Model.Authentication.RegisterResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IAuthenticateService {
    RegisterResponse register(RegisterDTO registerDTO);
    AuthResponse authenticate(AuthRequest authRequest);
    void saveUserToken(UserEntity user, String jwtToken);
    void revokeAllUserTokens(UserEntity user);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
