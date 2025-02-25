package hcmute.techshop.Controller;

import hcmute.techshop.Model.Authentication.AuthRequest;
import hcmute.techshop.Model.Authentication.AuthResponse;
import hcmute.techshop.Model.Authentication.RegisterDTO;
import hcmute.techshop.Model.Authentication.RegisterResponse;
import hcmute.techshop.Service.impl.AuthenticateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
