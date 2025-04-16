package hcmute.techshop.Service.Product.comment;

import java.util.List;

import hcmute.techshop.Model.Product.AddCommentRequest;
import hcmute.techshop.Model.Product.ProductCommentModel;
import hcmute.techshop.Model.Product.UpdateCommentRequest;

public interface IProductCommentService {

    // Thêm bình luận mới (không cần mua sản phẩm trước)
    ProductCommentModel addComment(AddCommentRequest request, String email);

    // Cập nhật bình luận (bao gồm cả bình luận đã ẩn của chính mình)
    ProductCommentModel updateComment(UpdateCommentRequest request, String email);

    // Bật/tắt trạng thái bình luận (chỉ chủ sở hữu mới được thực hiện)
    boolean toggleCommentStatus(Integer id, String email);

    // Lấy danh sách bình luận của một sản phẩm
    List<ProductCommentModel> getProductComments(Integer productId, String email);

    // Lấy danh sách tất cả bình luận của một sản phẩm (dành cho Admin)
    List<ProductCommentModel> getAllProductCommentsForAdmin(Integer productId, String adminEmail);

    //Lấy tất cả danh sách bình luận tất cả sản phẩm cho admin
    List<ProductCommentModel> getAllCommentsForAdmin(String adminEmail);

    // Lấy thông tin một bình luận cụ thể (bao gồm cả bình luận đã ẩn của chính
    // mình)
    ProductCommentModel getCommentById(Integer id, String email);

    // Xóa vĩnh viễn bình luận khỏi database (chỉ người tạo bình luận mới được xóa)
    boolean permanentDeleteComment(Integer id, String email);

    // Admin bật/tắt trạng thái một bình luận bất kỳ
    boolean adminToggleCommentStatus(Integer id);
}