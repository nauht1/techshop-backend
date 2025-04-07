package hcmute.techshop.Config.Aop;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Repository.Auth.UserTrackingRepository;
import hcmute.techshop.Repository.Cart.CartRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import lombok.AllArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Aspect
@Component
@AllArgsConstructor
public class UserTrackingAspect {
    private final UserTrackingRepository userTrackingRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private static final Map<String, Set<String>> PRODUCT_RELATED_ACTIONS = new HashMap<>();
    private static final Map<String, Set<String>> CART_RELATED_ACTIONS = new HashMap<>();

    static {
        PRODUCT_RELATED_ACTIONS.put("ProductAttributeController", Set.of("findAll", "findById"));
        PRODUCT_RELATED_ACTIONS.put("ProductCommentController", Set.of("addComment", "updateComment", "toggleCommentStatus", "getProductComments", "getCommentById", "permanentDeleteComment"));
        PRODUCT_RELATED_ACTIONS.put("ProductReviewController", Set.of("addReview", "updateReview", "toggleReviewStatus","getProductReviews","getReviewById", "permanentDeleteReview"));
        PRODUCT_RELATED_ACTIONS.put("ProductVariantController", Set.of("getProductVariants", "getProductVariantById"));
//        PRODUCT_RELATED_ACTIONS.put("CartController", Set.of("addToCart", "removeFromCart"));

    }
    @Before("execution(* hcmute.techshop.Controller..*.*(..))")
    public void trackUserAction(JoinPoint joinPoint) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        UserEntity currentUser = getUserFromServletFilter();

        if (currentUser.getId() == -1 ||currentUser.getRole() == Role.ROLE_ADMIN) {
            System.out.println("Skipping tracking for anonymous user || admin account");
            return;
        }
        String action = joinPoint.getSignature().toShortString();
        String apiPath = request.getRequestURI();
        String httpMethod = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        String requestParams = request.getQueryString() != null ? request.getQueryString() : "N/A";

        System.out.println("Aspect triggered for method: " + action);
        System.out.println("User: " + currentUser.getEmail() + ", API Path: " + apiPath + ", Method: " + httpMethod);

        String controllerName = extractControllerName(action);
        String methodName = extractMethodName(action);

         ProductEntity product = null;
        if (isProductRelatedAction(controllerName, methodName)) {
            String idName = controllerName.equals("ProductController") ? "id" : "productId";
            Integer productId = extractEntityId(joinPoint, request, idName);
            System.out.println(productId);
            if (productId != null) {
                product = productRepository.findById(productId).orElse(null);
            }
        }
        UserTracking userTracking = UserTracking
                .builder()
                .user(currentUser)
                .action(action)
                .apiPath(apiPath)
                .httpMethod(httpMethod)
                .ipAddress(ipAddress)
                .requestParams(requestParams)
                .timestamp(Instant.now())
                .product(product)
                .build();
        userTrackingRepository.save(userTracking);
    }

    private boolean isProductRelatedAction(String controllerName, String methodName) {
        Set<String> methods = PRODUCT_RELATED_ACTIONS.get(controllerName);
        return methods != null && methods.contains(methodName);
    }
    private String extractControllerName(String action) {
        int dotIndex = action.indexOf('.');
        if (dotIndex != -1) {
            return action.substring(0, dotIndex);
        }
        return "";
    }

    private String extractMethodName(String action) {
        int dotIndex = action.indexOf('.');
        int parenIndex = action.indexOf('(');
        if (dotIndex != -1 && parenIndex != -1) {
            return action.substring(dotIndex + 1, parenIndex);
        }
        return "";
    }
    private Integer extractEntityId(JoinPoint joinPoint, HttpServletRequest request, String idName) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            if (idName.equals(parameterNames[i])) {
                Object arg = args[i];
                if (arg instanceof Long) {
                    return (Integer) arg;
                } else if (arg instanceof Integer) {
                    return ((Integer) arg);
                } else if (arg instanceof String) {
                    try {
                        return Integer.parseInt((String) arg);
                    } catch (NumberFormatException e) {
                        // Ignore if parsing fails
                    }
                }
            }
        }

        Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        Map<String, String> uriTemplateVariables = new HashMap<>();

        if(attribute instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) attribute;

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                    uriTemplateVariables.put((String) entry.getKey(), (String) entry.getValue());
                }
            }
            System.out.println("uri" + uriTemplateVariables);
            if (!uriTemplateVariables.isEmpty()) {
                String idStr = uriTemplateVariables.get(idName);
                if (idStr == null && "id".equals(idName)) {
                    idStr = uriTemplateVariables.get(idName);
                }
                if (idStr != null) {
                    try {
                        return Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        // Ignore if parsing fails
                    }
                }
            }
        }
        String idParam = request.getParameter(idName);
        if (idParam != null) {
            try {
                return Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                // Ignore if parsing fails
            }
        }
        return null;
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
