package hcmute.techshop.Service.User;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Model.User.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    ProfileResponse updateUserProfileService(Integer id, ProfileRequest request);
    ProfileResponse getProfileUser(Integer id);
    void deleteUser(Integer id);
    void updateUserAvatar(Integer id, MultipartFile avatar);
}
