package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Product.AddCommentRequest;
import hcmute.techshop.Model.Product.ProductCommentModel;
import hcmute.techshop.Model.Product.UpdateCommentRequest;
import hcmute.techshop.Service.Product.comment.IProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
public class ProductCommentController {

    private final IProductCommentService productCommentService;
    
    // Thêm bình luận chính user đó không cần mua sản phẩm có thể thêm nhiều bình luận
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCommentModel>> addComment(
            @RequestBody AddCommentRequest request,
            Authentication authentication) {
        try {
            // Kiểm tra giá trị productId
            if (request.getProductId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Sản phẩm không được bỏ trống", null));
            }
            
            // Kiểm tra nội dung bình luận
            if (request.getComment() == null || request.getComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Nội dung bình luận không được bỏ trống", null));
            }

            ProductCommentModel result = productCommentService.addComment(request, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã thêm bình luận thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi thêm bình luận: " + e.getMessage(), null));
        }
    }

    // Cập nhật bình luận chính user đó (bao gồm cả bình luận đã ẩn)
    @PutMapping
    public ResponseEntity<ApiResponse<ProductCommentModel>> updateComment(
            @RequestBody UpdateCommentRequest request,
            Authentication authentication) {
        try {
            // Kiểm tra giá trị id
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "ID bình luận không được bỏ trống", null));
            }
            
            // Kiểm tra nội dung bình luận
            if (request.getComment() == null || request.getComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Nội dung bình luận không được bỏ trống", null));
            }

            ProductCommentModel result = productCommentService.updateComment(request, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã cập nhật bình luận thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi cập nhật bình luận: " + e.getMessage(), null));
        }
    }
    
    // Bật/tắt trạng thái bình luận của chính user đó
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleCommentStatus(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            boolean isActive = productCommentService.toggleCommentStatus(id, authentication.getName());
            String message = isActive ? "Đã hiện bình luận thành công" : "Đã ẩn bình luận thành công";
            return ResponseEntity.ok(new ApiResponse<>(true, message, isActive));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi thay đổi trạng thái bình luận: " + e.getMessage(), false));
        }
    }
    
    // Lấy danh sách bình luận của một sản phẩm (bao gồm cả bình luận đã ẩn của chính mình)
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductCommentModel>>> getProductComments(
            @PathVariable Integer productId, 
            Authentication authentication) {
        try {
            List<ProductCommentModel> comments = productCommentService.getProductComments(productId, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách bình luận thành công", comments));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách bình luận: " + e.getMessage(), null));
        }
    }
    
   // Lấy thông tin một bình luận cụ thể (bao gồm cả bình luận đã ẩn của chính mình)
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
    
    // Xóa vĩnh viễn bình luận (chỉ user có thể xóa bình luận của chính họ)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse<Boolean>> permanentDeleteComment(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            boolean result = productCommentService.permanentDeleteComment(id, authentication.getName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Đã xóa vĩnh viễn bình luận thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa vĩnh viễn bình luận: " + e.getMessage(), false));
        }
    }
}