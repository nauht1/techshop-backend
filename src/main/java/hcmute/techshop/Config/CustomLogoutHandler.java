package hcmute.techshop.Config;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Service.Tracking.TrackingService;
import hcmute.techshop.Service.User.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TrackingService trackingService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            UserEntity user = (UserEntity) authentication.getPrincipal();

            trackingService.track(user, EventType.LOGOUT, "User logged out");
        }

    }
}
