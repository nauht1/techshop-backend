package hcmute.techshop.Service.User;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Model.User.ProfileRequest;
import hcmute.techshop.Model.User.ProfileResponse;

public interface IUserService {
    ProfileResponse updateUserProfileService(Integer id, ProfileRequest request);
    ProfileResponse getProfileUser(Integer id);
}
