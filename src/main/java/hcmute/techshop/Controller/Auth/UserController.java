package hcmute.techshop.Controller.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getProfileUser(id));
    }

    @PutMapping("/profile/update/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Integer id,@RequestBody ProfileRequest profileRequest) {
        userService.updateUserProfileService(id, profileRequest);
        return ResponseEntity.ok(userService.updateUserProfileService(id, profileRequest));
    }
}
