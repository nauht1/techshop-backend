package hcmute.techshop.Config.oauth2;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Service.Auth.OAuth2ImplementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;


@Service
public class ImplementOauth2UserService extends DefaultOAuth2UserService {


    private final OAuth2ImplementService oAuth2ImplementService;

    public ImplementOauth2UserService(OAuth2ImplementService oAuth2ImplementService) {
        this.oAuth2ImplementService = oAuth2ImplementService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(userRequest, oAuth2User);
        }
        catch (AuthenticationException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from Oauth2 provider");
        }
        UserEntity user = oAuth2ImplementService.storageUser(oAuth2UserInfo, oAuth2UserRequest);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "email"
        );
    }

}