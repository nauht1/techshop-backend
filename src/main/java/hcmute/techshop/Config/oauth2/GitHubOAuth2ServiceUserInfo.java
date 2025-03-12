package hcmute.techshop.Config.oauth2;

import java.util.Map;

public class GitHubOAuth2ServiceUserInfo  extends OAuth2UserInfo{
    public GitHubOAuth2ServiceUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
