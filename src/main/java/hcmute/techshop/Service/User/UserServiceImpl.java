package hcmute.techshop.Service.User;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Mapper.UserMapper;
import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Model.User.ProfileResponse;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Auth.UserTrackingRepository;
import hcmute.techshop.Service.UploadFile.UploadFileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserTrackingRepository userTrackingRepository;
    private final ModelMapper modelMapper;
    private final UploadFileServiceImpl uploadFileService;
    @Override
    @Transactional
    public ProfileResponse updateUserProfileService(Integer id, ProfileRequest request) {

        try {
            ChecAuthenticationForCurrentUser(id);

            UserEntity user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userMapper.updateUserFromUser(user, request);
            userRepository.save(user);
            return ProfileResponse.builder().message("Update profile successful").user(mapperUserToUserModel(user)).build();
        }catch (RuntimeException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public ProfileResponse getProfileUser(Integer id) {
        UserEntity currentUser = ChecAuthenticationForCurrentUser(id);
        return ProfileResponse.builder().message("Update profile successful").user(mapperUserToUserModel(currentUser)).build();
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
    private UserModel mapperUserToUserModel(UserEntity user) {
        return modelMapper.map(user, UserModel.class);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id.longValue())) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id.longValue());
    }

    @Override
    public void updateUserAvatar(Integer id, MultipartFile avatar) {
        try {
            UserEntity user = ChecAuthenticationForCurrentUser(id);
            if (avatar == null || avatar.isEmpty()) {
                throw new IllegalArgumentException("Avatar file cannot be empty.");
            }
            System.out.println("checked is successflly");
            String contentType = avatar.getContentType();

            System.out.println("check content" + contentType);
            if (!contentType.startsWith("image/")) {
                throw new IllegalArgumentException("File must be an image.");
            }

            String avatarUrl = uploadFileService.uploadImageMultipart(avatar);
            System.out.println("avatar url " + avatarUrl);
            user.setAvatar(avatarUrl);
            userRepository.save(user);
        } catch (RuntimeException exception) {
            throw new RuntimeException(exception.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Page<UserTracking> getUserActivity(Integer userId, PageRequest pageRequest) {
        return userTrackingRepository.findByUserId(userId.longValue(), pageRequest);
    }
}
