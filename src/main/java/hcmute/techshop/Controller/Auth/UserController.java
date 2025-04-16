package hcmute.techshop.Controller.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Model.User.ProfileResponse;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/profile")
    public ResponseEntity<ResponseModel> getCurrentUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                throw new IllegalStateException("User not authenticated");
            }
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            ProfileResponse userProfile = userService.getProfileUser(currentUser.getId().intValue());
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get user profile successfully")
                            .body(userProfile)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to get user profile: " + e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/activity")
    public ResponseEntity<ResponseModel> getUserActivity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp,desc") String sort) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                throw new IllegalStateException("User not authenticated");
            }
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();

            String[] sortParams = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            Sort sortBy = Sort.by(direction, sortParams[0]);
            PageRequest pageRequest = PageRequest.of(page, size, sortBy);

            Page<UserTracking> activityPage = userService.getUserActivity(currentUser.getId().intValue(), pageRequest);

            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get user activity successfully")
                            .body(Map.of(
                                    "content", activityPage.getContent(),
                                    "totalElements", activityPage.getTotalElements(),
                                    "totalPages", activityPage.getTotalPages(),
                                    "currentPage", activityPage.getNumber()
                            ))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to get user activity: " + e.getMessage())
                            .build()
            );
        }
    }
}
