package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Product.AddReviewRequest;
import hcmute.techshop.Model.Product.ProductReviewModel;
import hcmute.techshop.Model.Product.UpdateReviewRequest;
import hcmute.techshop.Service.Product.review.IProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ProductReviewController {

    private final IProductReviewService productReviewService;

    // Thêm đánh giá sản phẩm (yêu cầu đăng nhập và đã mua sản phẩm)
    @PostMapping
    public ResponseEntity<ApiResponse<ProductReviewModel>> addReview(
            @RequestBody AddReviewRequest request,
            Authentication authentication) {
        try {
            // Kiểm tra giá trị rating
            if (request.getRating() < 1 || request.getRating() > 5) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Rating phải từ 1 đến 5", null));
            }
            if (request.getProductId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Product ID không được để trống", null));
            }

            ProductReviewModel result = productReviewService.addReview(request, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã thêm đánh giá thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi thêm đánh giá: " + e.getMessage(), null));
        }
    }

    // Cập nhật đánh giá sản phẩm (bao gồm cả đánh giá đã ẩn chính user đó)
    @PutMapping
    public ResponseEntity<ApiResponse<ProductReviewModel>> updateReview(
            @RequestBody UpdateReviewRequest request,
            Authentication authentication) {
        try {
            // Kiểm tra giá trị id
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "ID không được trống", null));
            }
            
            // Kiểm tra giá trị rating
            if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Rating phải từ 1 đến 5", null));
            }

            ProductReviewModel result = productReviewService.updateReview(request, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã cập nhật đánh giá thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật đánh giá: " + e.getMessage(), null));
        }
    }

    // Bật/tắt trạng thái đánh giá sản phẩm của chính user đó
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleReviewStatus(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            boolean isActive = productReviewService.toggleReviewStatus(id, authentication.getName());
            String message = isActive ? "Đã hiện đánh giá thành công" : "Đã ẩn đánh giá thành công";
            return ResponseEntity.ok(new ApiResponse<>(true, message, isActive));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi thay đổi trạng thái đánh giá: " + e.getMessage(), false));
        }
    }

    // Lấy danh sách đánh giá của một sản phẩm (bao gồm cả đánh giá đã ẩn của chính mình)
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductReviewModel>>> getProductReviews(
            @PathVariable Integer productId,
            Authentication authentication) {
        try {
            List<ProductReviewModel> reviews = productReviewService.getProductReviews(productId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách đánh giá thành công", reviews));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách đánh giá: " + e.getMessage(), null));
        }
    }

    // Lấy thông tin một đánh giá cụ thể (bao gồm cả đánh giá đã ẩn của chính mình)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductReviewModel>> getReviewById(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            ProductReviewModel review = productReviewService.getReviewById(id, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thông tin đánh giá thành công", review));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy thông tin đánh giá: " + e.getMessage(), null));
        }
    }

    // Xóa vĩnh viễn đánh giá (chỉ user có thể xóa đánh giá của chính họ)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Boolean>> permanentDeleteReview(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            boolean result = productReviewService.permanentDeleteReview(id, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã xóa vĩnh viễn đánh giá thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa vĩnh viễn đánh giá: " + e.getMessage(), false));
        }
    }
}