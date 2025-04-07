package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Product.ProductCommentModel;
import hcmute.techshop.Service.Product.comment.IProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/comments")
@RequiredArgsConstructor
public class AdminProductCommentController {

    private final IProductCommentService productCommentService;

    // Lấy tất cả danh sách bình luận tất cả sản phẩm cho admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProductCommentModel>>> getAllCommentsForAdmin(Authentication authentication) {
        try {
            List<ProductCommentModel> comments = productCommentService.getAllCommentsForAdmin(authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách bình luận thành công", comments));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách bình luận: " + e.getMessage(), null));
        }
    }

    // Lấy danh sách bình luận của một sản phẩm (admin có thể xem tất cả bình luận kể cả bị ẩn)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductCommentModel>>> getProductComments(
            @PathVariable Integer productId,
            Authentication authentication) {
        try {
            List<ProductCommentModel> comments = productCommentService.getAllProductCommentsForAdmin(productId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách bình luận thành công", comments));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách bình luận: " + e.getMessage(), null));
        }
    }

    // Lấy thông tin một bình luận cụ thể - admin có thể xem tất cả bình luận theo id
    // Sử dụng chung phương thức getCommentById với controller người dùng
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCommentModel>> getCommentById(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            ProductCommentModel comment = productCommentService.getCommentById(id, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thông tin bình luận thành công", comment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy thông tin bình luận: " + e.getMessage(), null));
        }
    }
    
    // Admin bật/tắt trạng thái của một bình luận bất kỳ
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleCommentStatus(@PathVariable Integer id) {
        try {
            boolean isActive = productCommentService.adminToggleCommentStatus(id);
            String message = isActive ? "Đã hiện bình luận thành công" : "Đã ẩn bình luận thành công";
            return ResponseEntity.ok(new ApiResponse<>(true, message, isActive));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi thay đổi trạng thái bình luận: " + e.getMessage(), false));
        }
    }
    
}