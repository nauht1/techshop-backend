package hcmute.techshop.Service.Product;

import hcmute.techshop.Model.Product.AddCommentRequest;
import hcmute.techshop.Model.Product.ProductCommentModel;
import hcmute.techshop.Model.Product.UpdateCommentRequest;

import java.util.List;

public interface ProductCommentService {

    // Thêm bình luận mới
    ProductCommentModel addComment(AddCommentRequest request, String email);
    
    // Cập nhật bình luận (bao gồm cả bình luận đã ẩn)
    ProductCommentModel updateComment(UpdateCommentRequest request, String email);
    
    // Xóa ẩn bình luận (soft delete)
    boolean deleteComment(Integer id, String email);
    
    // Khôi phục bình luận đã ẩn
    boolean restoreComment(Integer id, String email);
    
    // Lấy danh sách bình luận của một sản phẩm
    List<ProductCommentModel> getProductComments(Integer productId, String email);
    
    // Lấy thông tin bình luận (bao gồm cả bình luận đã ẩn của chính mình)
    ProductCommentModel getCommentById(Integer id, String email);
    
    // Xóa vĩnh viễn bình luận khỏi database (chỉ người tạo bình luận mới được xóa)
    boolean permanentDeleteComment(Integer id, String email);
} 