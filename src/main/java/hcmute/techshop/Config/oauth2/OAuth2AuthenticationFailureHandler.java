package hcmute.techshop.Config.oauth2;

import hcmute.techshop.Util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.security.sasl.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final HttpCookieOauth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public OAuth2AuthenticationFailureHandler(HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOauth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException {
            String targetUrl = CookieUtils.getCookie(request, HttpCookieOauth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                    .map(Cookie::getValue)
                    .orElse(("/"));

            targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", exception.getLocalizedMessage())
                    .build().toUriString();

            httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
