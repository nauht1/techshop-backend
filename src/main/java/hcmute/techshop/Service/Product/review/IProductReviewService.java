package hcmute.techshop.Service.Product.review;

import hcmute.techshop.Model.Product.AddReviewRequest;
import hcmute.techshop.Model.Product.ProductReviewModel;
import hcmute.techshop.Model.Product.UpdateReviewRequest;

import java.util.List;

public interface IProductReviewService {
    // Thêm đánh giá sản phẩm (yêu cầu người dùng đã mua sản phẩm)
    ProductReviewModel addReview(AddReviewRequest request, String username);
    
    // Cập nhật đánh giá sản phẩm (chỉ chủ sở hữu mới được cập nhật)
    ProductReviewModel updateReview(UpdateReviewRequest request, String username);
    
    // Bật/tắt trạng thái đánh giá sản phẩm (chỉ chủ sở hữu mới được thực hiện)
    boolean toggleReviewStatus(Integer id, String username);
    
    // Lấy danh sách đánh giá của một sản phẩm
    List<ProductReviewModel> getProductReviews(Integer productId, String username);
    
    // Lấy danh sách tất cả đánh giá của một sản phẩm (dành cho Admin)
    List<ProductReviewModel> getAllProductReviewsForAdmin(Integer productId, String adminEmail);

    //Lấy tất cả danh sách đánh giá tất cả sản phẩm cho admin
    List<ProductReviewModel> getAllReviewsForAdmin(String adminEmail);
    
    // Lấy thông tin một đánh giá cụ thể (dùng chung cho cả user và admin)
    ProductReviewModel getReviewById(Integer id, String username);
    
    // Xóa vĩnh viễn đánh giá khỏi database (chỉ người tạo đánh giá mới được xóa) 
    boolean permanentDeleteReview(Integer id, String username);
    
    // Admin bật/tắt trạng thái một đánh giá bất kỳ
    boolean adminToggleReviewStatus(Integer id);
}