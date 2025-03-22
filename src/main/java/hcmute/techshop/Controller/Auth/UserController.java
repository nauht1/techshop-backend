package hcmute.techshop.Controller.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.User.ProfileResponse;
import hcmute.techshop.Repository.Auth.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userRepository.save(user);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot found user in system!!!"));
        return ResponseEntity.ok(ProfileResponse.builder().message("Get profile user success").user(user).build());
    }

    @PutMapping("/profile/update/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id) {

    }
}
