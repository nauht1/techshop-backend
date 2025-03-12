package hcmute.techshop.Config;

import com.quocanh.doan.Exception.OAuth2AuthenticationProcessingException;
import com.quocanh.doan.Model.User;
import com.quocanh.doan.Service.ImplementService.OAuth2.OAuth2ImplementService;
import com.quocanh.doan.Service.ImplementService.User.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


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
            throw new OAuth2AuthenticationProcessingException("Email not found from Oauth2 provider");
        }
        User user = oAuth2ImplementService.storageUser(oAuth2UserInfo, oAuth2UserRequest);

        return UserPrincipal.build(user, oAuth2User.getAttributes());
    }

}