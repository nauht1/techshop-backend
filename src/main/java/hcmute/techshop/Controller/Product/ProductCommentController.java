package hcmute.techshop.Controller.Product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hcmute.techshop.Model.Product.AddCommentRequest;
import hcmute.techshop.Model.Product.ProductCommentModel;
import hcmute.techshop.Model.Product.UpdateCommentRequest;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.comment.IProductCommentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
public class ProductCommentController {

    private final IProductCommentService productCommentService;
    
    // Thêm bình luận chính user đó không cần mua sản phẩm có thể thêm nhiều bình luận
    @PostMapping
    public ResponseEntity<ResponseModel> addComment(
            @RequestBody AddCommentRequest request,
            Authentication authentication) {
        try {
            // Kiểm tra giá trị productId
            if (request.getProductId() == null) {
                return ResponseEntity.badRequest().body(new ResponseModel(false, "Sản phẩm không được bỏ trống", null));
            }
            
            // Kiểm tra nội dung bình luận
            if (request.getComment() == null || request.getComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseModel(false, "Nội dung bình luận không được bỏ trống", null));
            }

            ProductCommentModel result = productCommentService.addComment(request, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Đã thêm bình luận thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi thêm bình luận: " + e.getMessage(), null));
        }
    }

    // Cập nhật bình luận chính user đó (bao gồm cả bình luận đã ẩn)
    @PutMapping
    public ResponseEntity<ResponseModel> updateComment(
            @RequestBody UpdateCommentRequest request,
            Authentication authentication) {
        try {
            // Kiểm tra giá trị id
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new ResponseModel(false, "ID bình luận không được bỏ trống", null));
            }
            
            // Kiểm tra nội dung bình luận
            if (request.getComment() == null || request.getComment().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(new ResponseModel(false, "Nội dung bình luận không được bỏ trống", null));
            }

            ProductCommentModel result = productCommentService.updateComment(request, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Đã cập nhật bình luận thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi cập nhật bình luận: " + e.getMessage(), null));
        }
    }
    
    // Bật/tắt trạng thái bình luận của chính user đó
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ResponseModel> toggleCommentStatus(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            boolean isActive = productCommentService.toggleCommentStatus(id, authentication.getName());
            String message = isActive ? "Đã hiện bình luận thành công" : "Đã ẩn bình luận thành công";
            return ResponseEntity.ok(new ResponseModel(true, message, isActive));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), false));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi thay đổi trạng thái bình luận: " + e.getMessage(), false));
        }
    }
    
    // Lấy danh sách bình luận của một sản phẩm (bao gồm cả bình luận đã ẩn của chính mình)
    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseModel> getProductComments(
            @PathVariable Integer productId, 
            Authentication authentication) {
        try {
            List<ProductCommentModel> comments = productCommentService.getProductComments(productId, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Lấy danh sách bình luận thành công", comments));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi lấy danh sách bình luận: " + e.getMessage(), null));
        }
    }
    
   // Lấy thông tin một bình luận cụ thể (bao gồm cả bình luận đã ẩn của chính mình)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getCommentById(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            ProductCommentModel comment = productCommentService.getCommentById(id, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Lấy thông tin bình luận thành công", comment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi lấy thông tin bình luận: " + e.getMessage(), null));
        }
    }
    
    // Xóa vĩnh viễn bình luận (chỉ user có thể xóa bình luận của chính họ)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ResponseModel> permanentDeleteComment(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            boolean result = productCommentService.permanentDeleteComment(id, authentication.getName());
            return ResponseEntity.ok(new ResponseModel(true, "Đã xóa vĩnh viễn bình luận thành công", result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseModel(false, e.getMessage(), false));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseModel(false, e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi khi xóa vĩnh viễn bình luận: " + e.getMessage(), false));
        }
    }
}