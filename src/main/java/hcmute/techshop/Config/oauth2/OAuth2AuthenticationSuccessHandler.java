package hcmute.techshop.Config.oauth2;

import hcmute.techshop.Entity.Auth.TokenEntity;
import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.TokenType;
import hcmute.techshop.Repository.Auth.TokenRepository;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.Auth.JwtService;
import hcmute.techshop.Util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;
    private final AppProperties appProperties;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService tokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUrl = CookieUtils.getCookie(request, HttpCookieOauth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if(redirectUrl.isPresent() && !isAuthorizeRedirectUri(redirectUrl.get())) {
            throw new RuntimeException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUri = redirectUrl.orElse(getDefaultTargetUrl());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email"); // Lấy email từ OAuth2 provider

        // Load user từ database
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot found user!!!"));
        var jwtToken = tokenProvider.generateToken(user);
        var refreshToken = tokenProvider.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return UriComponentsBuilder.fromUriString(targetUri)
                .queryParam("accessToken", jwtToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOauth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
    public void saveUserToken(UserEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    public void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private boolean isAuthorizeRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream().
                anyMatch(
                        redirectUri -> {
                            URI authorizeURI = URI.create(redirectUri);
                            return authorizeURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                                    && authorizeURI.getPort() == clientRedirectUri.getPort();
                        }
                );

    }
}