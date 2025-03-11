package hcmute.techshop.Controller.Auth;

import hcmute.techshop.Model.Auth.AuthRequest;
import hcmute.techshop.Model.Auth.RegisterDTO;
import hcmute.techshop.Model.Auth.RegisterResponse;
import hcmute.techshop.Model.Auth.VerifyCodeRequest;
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
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(authenticateService.register(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticateService.authenticate(authRequest));
    }

    @PutMapping("/check-code")
    public ResponseEntity<?> checkCode(@RequestBody VerifyCodeRequest request) {
        authenticateService.VerifiedCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
