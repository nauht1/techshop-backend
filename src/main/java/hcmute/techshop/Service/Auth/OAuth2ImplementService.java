package hcmute.techshop.Service.Auth;

import hcmute.techshop.Config.oauth2.OAuth2UserInfo;
import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.AuthProvider;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Repository.Auth.UserRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OAuth2ImplementService implements Oauth2ServiceLayer {

    private final UserRepository userRepository;
    public  OAuth2ImplementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserEntity storageUser(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        UserEntity user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new RuntimeException(
                        "Looks like you're signup with" + user.getProvider() + " account. Please use your " + user.getProvider() +
                                " account to login."
                );
            }
            user = updateExistingUser(user, oAuth2UserInfo);

        } else {
            user = this.registerUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return user;
    }
    @Override
    public UserEntity registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        var user = UserEntity.builder()
                .email(oAuth2UserInfo.getEmail())
                .username(oAuth2UserInfo.getName())
                .providerId(oAuth2UserInfo.getId())
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .isActive(false)
                .role(Role.ROLE_CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .checkCode(true)
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }


}