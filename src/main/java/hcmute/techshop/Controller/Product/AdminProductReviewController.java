package hcmute.techshop.Controller.Product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.techshop.Model.Product.ProductReviewModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.review.IProductReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/reviews")
@RequiredArgsConstructor
public class AdminProductReviewController {

    private final IProductReviewService productReviewService;

    // Lấy tất cả danh sách đánh giá tất cả sản phẩm cho admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ResponseModel> getAllReviewsForAdmin(Authentication authentication) {
        try {
            List<ProductReviewModel> reviews = productReviewService.getAllReviewsForAdmin(authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách đánh giá thành công", reviews));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi lấy danh sách đánh giá: " + e.getMessage(), null));
        }
    }

    // Lấy danh sách đánh giá của một sản phẩm (admin có thể xem tất cả đánh giá kể cả bị ẩn)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseModel> getProductReviews(
            @PathVariable Integer productId,
            Authentication authentication) {
        try {
            List<ProductReviewModel> reviews = productReviewService.getAllProductReviewsForAdmin(productId, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách đánh giá thành công", reviews));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi lấy danh sách đánh giá: " + e.getMessage(), null));
        }
    }

    // Lấy thông tin một đánh giá cụ thể - admin có thể xem tất cả đánh giá theo id
    // Sử dụng chung phương thức getReviewById với controller người dùng
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getReviewById(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            ProductReviewModel review = productReviewService.getReviewById(id, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin đánh giá thành công", review));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi lấy thông tin đánh giá: " + e.getMessage(), null));
        }
    }

    // Admin bật/tắt trạng thái của một đánh giá bất kỳ
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ResponseModel> toggleReviewStatus(@PathVariable Integer id) {
        try {
            boolean isActive = productReviewService.adminToggleReviewStatus(id);
            String message = isActive ? "Đã hiện đánh giá thành công" : "Đã ẩn đánh giá thành công";
            return ResponseEntity.ok(new ResponseModel(true, message, isActive));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi thay đổi trạng thái đánh giá: " + e.getMessage(), false));
        }
    }

}