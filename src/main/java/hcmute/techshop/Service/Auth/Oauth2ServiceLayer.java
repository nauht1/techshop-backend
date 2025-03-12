package hcmute.techshop.Service.Auth;

import hcmute.techshop.Config.oauth2.OAuth2UserInfo;
import hcmute.techshop.Entity.Auth.UserEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

public interface Oauth2ServiceLayer {
    UserEntity registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo);
    UserEntity updateExistingUser(UserEntity existingUser, OAuth2UserInfo oAuth2UserInfo);
    UserEntity storageUser(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest);
}