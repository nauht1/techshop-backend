package hcmute.techshop.Service.User;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Mapper.UserMapper;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Model.User.ProfileResponse;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Auth.UserTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserTrackingRepository userTrackingRepository;
    @Override
    @Transactional
    public ProfileResponse updateUserProfileService(Integer id, ProfileRequest request) {

        try {
            ChecAuthenticationForCurrentUser(id);

            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userMapper.updateUserFromUser(user, request);
            userRepository.save(user);
            return ProfileResponse.builder().message("Update profile successful").user(user).build();
        }catch (RuntimeException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public ProfileResponse getProfileUser(Integer id) {
        UserEntity currentUser = ChecAuthenticationForCurrentUser(id);
        return ProfileResponse.builder().message("Update profile successful").user(currentUser).build();
    }

    private UserEntity ChecAuthenticationForCurrentUser(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("You are not authenticated.");
        }
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You are not authorized to access this profile.");
        }
        return currentUser;
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id.longValue())) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id.longValue());
    }

    public Page<UserTracking> getUserActivity(Integer userId, PageRequest pageRequest) {
        return userTrackingRepository.findByUserId(userId.longValue(), pageRequest);
    }
}
