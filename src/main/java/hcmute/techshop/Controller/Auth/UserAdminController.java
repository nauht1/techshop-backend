package hcmute.techshop.Controller.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Model.User.ProfileResponse;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.User.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseModel> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        try {
            String[] sortParams = sort.split(",");
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            Sort sortBy = Sort.by(direction, sortParams[0]);
            PageRequest pageRequest = PageRequest.of(page, size, sortBy);

            Page<UserEntity> userPage = userRepository.findAll(pageRequest);

            List<UserModel> users = userPage.getContent().stream()
                    .map(user -> modelMapper.map(user, UserModel.class))
                    .toList();

            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get all users successfully")
                            .body(Map.of(
                                    "content", users,
                                    "totalElements", userPage.getTotalElements(),
                                    "totalPages", userPage.getTotalPages(),
                                    "currentPage", userPage.getNumber()
                            ))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to get users: " + e.getMessage())
                            .build()
            );
        }
    }
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getProfileUser(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("User deleted successfully")
                            .body(null)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to delete user: " + e.getMessage())
                            .build()
            );
        }
    }
    @PutMapping("/profile/update/{id}")
    public ResponseEntity<ResponseModel> updateUserProfile(@PathVariable Integer id, @RequestBody ProfileRequest profileRequest) {
        try {
            ProfileResponse updatedUser = userService.updateUserProfileService(id, profileRequest);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("User profile updated successfully")
                            .body(updatedUser)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message("Failed to update user profile: " + e.getMessage())
                            .build()
            );
        }
    }
}
