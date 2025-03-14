package hcmute.techshop.Service.Product;

import hcmute.techshop.Model.Product.AddReviewRequest;
import hcmute.techshop.Model.Product.ProductReviewModel;
import hcmute.techshop.Model.Product.UpdateReviewRequest;

import java.util.List;

public interface ProductReviewService {
    // Thêm đánh giá sản phẩm (yêu cầu người dùng đã mua sản phẩm)
    ProductReviewModel addReview(AddReviewRequest request, String username);
    
    // Cập nhật đánh giá sản phẩm (chỉ chủ sở hữu mới được cập nhật)
    ProductReviewModel updateReview(UpdateReviewRequest request, String username);
    
    // Xóa đánh giá sản phẩm (chỉ chủ sở hữu mới được xóa)
    boolean deleteReview(Integer id, String username);
    
    // Khôi phục đánh giá sản phẩm (chỉ chủ sở hữu mới được khôi phục)
    boolean restoreReview(Integer id, String username);
    
    // Lấy danh sách đánh giá của một sản phẩm
    List<ProductReviewModel> getProductReviews(Integer productId, String username);
    
    // Lấy thông tin một đánh giá cụ thể
    ProductReviewModel getReviewById(Integer id, String username);
    
    // Xóa vĩnh viễn đánh giá khỏi database (chỉ người tạo đánh giá mới được xóa) 
    boolean permanentDeleteReview(Integer id, String username);
} 