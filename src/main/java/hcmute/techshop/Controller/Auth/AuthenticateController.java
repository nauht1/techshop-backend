package hcmute.techshop.Controller.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Auth.*;
import hcmute.techshop.Service.Auth.AuthenticateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticateServiceImpl authenticateService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        UserEntity user = new UserEntity();
        System.out.println(registerDTO);
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());

        authenticateService.register(registerDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticateService.authenticate(authRequest));
    }

    @PutMapping("/check-code")
    public ResponseEntity<?> checkCode(@RequestBody VerifyCodeRequest request) {
        return ResponseEntity.ok(authenticateService.VerifiedCode(request.getEmail(), request.getCode()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        return ResponseEntity.ok(authenticateService.forgotPassword(forgotPasswordRequest.getEmail()));
    }
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authenticateService.resetPasswordResponse(request.getPassword(), request.getToken()));
    }
}
