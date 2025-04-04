package hcmute.techshop.Config.Aop;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Repository.Auth.UserTrackingRepository;
import lombok.AllArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Objects;

@Aspect
@Component
@AllArgsConstructor
public class UserTrackingAspect {
    private final UserTrackingRepository userTrackingRepository;

    @Before("execution(* hcmute.techshop.Controller..*.*(..))")
    public void trackUserAction(JoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        UserEntity currentUser = getUserFromServletFilter();

        if (currentUser.getId() == -1) {
            System.out.println("Skipping tracking for anonymous user");
            return;
        }
        String action = joinPoint.getSignature().toShortString();
        String timestamp = Instant.now().toString();
        String apiPath = request.getRequestURI();
        String httpMethod = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        String requestParams = request.getQueryString() != null ? request.getQueryString() : "N/A";

        System.out.println("Aspect triggered for method: " + action);
        System.out.println("User: " + currentUser.getEmail() + ", API Path: " + apiPath + ", Method: " + httpMethod);

        UserTracking userTracking = UserTracking
                .builder()
                .user(currentUser)
                .action(action)
                .apiPath(apiPath)
                .httpMethod(httpMethod)
                .ipAddress(ipAddress)
                .requestParams(requestParams)
                .timestamp(timestamp)
                .build();
        userTrackingRepository.save(userTracking);
    }

    public UserEntity getUserFromServletFilter() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            UserEntity anonymousUser = new UserEntity();
            anonymousUser.setId(-1); // Dummy ID for anonymous
            anonymousUser.setUsername("anonymous");
            return anonymousUser;
        }
        return (UserEntity) authentication.getPrincipal();
    }
}
