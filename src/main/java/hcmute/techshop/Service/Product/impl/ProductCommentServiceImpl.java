package hcmute.techshop.Service.Product.impl;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductCommentEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Model.Product.AddCommentRequest;
import hcmute.techshop.Model.Product.ProductCommentModel;
import hcmute.techshop.Model.Product.UpdateCommentRequest;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Product.ProductCommentRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Service.Product.ProductCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCommentServiceImpl implements ProductCommentService {

    private final ProductCommentRepository productCommentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ProductCommentModel addComment(AddCommentRequest request, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        
        // Lấy thông tin sản phẩm
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
        
        // Tạo bình luận mới
        ProductCommentEntity comment = new ProductCommentEntity();
        comment.setUser(user);
        comment.setProduct(product);
        comment.setComment(request.getComment());
        comment.setActive(true);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        
        // Lưu vào database
        comment = productCommentRepository.save(comment);
        
        // Chuyển đổi sang model và trả về
        ProductCommentModel model = mapToModel(comment);
        model.setOwner(true);
        return model;
    }

    @Override
    public ProductCommentModel updateComment(UpdateCommentRequest request, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        
        // Lấy thông tin bình luận
        ProductCommentEntity comment = productCommentRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));
        
        // Kiểm tra quyền cập nhật (chỉ chủ sở hữu mới được cập nhật)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền cập nhật bình luận này");
        }
        
        // Cập nhật thông tin bình luận
        comment.setComment(request.getComment());
        comment.setUpdatedAt(LocalDateTime.now());
        
        // Lưu vào database
        comment = productCommentRepository.save(comment);
        
        // Chuyển đổi sang model và trả về
        ProductCommentModel model = mapToModel(comment);
        model.setOwner(true);
        return model;
    }

    @Override
    public boolean deleteComment(Integer id, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        
        // Lấy thông tin bình luận
        ProductCommentEntity comment = productCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));
        
        // Kiểm tra quyền xóa (chỉ chủ sở hữu mới được xóa)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền xóa bình luận này");
        }
        
        // Đánh dấu là không còn hoạt động (soft delete)
        comment.setActive(false);
        comment.setUpdatedAt(LocalDateTime.now());
        productCommentRepository.save(comment);
        return true;
    }

    @Override
    public List<ProductCommentModel> getProductComments(Integer productId, String email) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
        
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        
        List<ProductCommentEntity> comments = productCommentRepository.findByProduct(product);
        
        // Kiểm tra xem người dùng có phải là Admin không
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);
        
        return comments.stream()
                .filter(comment -> isAdmin || comment.isActive() || comment.getUser().getId().equals(user.getId()))
                .map(comment -> {
                    ProductCommentModel model = mapToModel(comment);
                    model.setOwner(comment.getUser().getId().equals(user.getId()));
                    return model;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductCommentModel getCommentById(Integer id, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Lấy thông tin bình luận
        ProductCommentEntity comment = productCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));

        // Kiểm tra xem người dùng có phải là Admin không
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);
        boolean isOwner =   comment.getUser().getId().equals(user.getId());

        // Logic quyền truy cập mới:
        // Admin có thể xem tất cả
        // Người dùng thường chỉ có thể xem bình luận không bị ẩn HOẶC bình luận do chính họ tạo (kể cả khi đã bị ẩn)
        if (!isAdmin && !(comment.isActive() || isOwner)) {
            throw new IllegalArgumentException("Bạn không có quyền xem bình luận này");
        }

        // Chuyển đổi sang model và trả về
        ProductCommentModel model = mapToModel(comment);
        model.setOwner(isOwner);
        return model;
    }

    @Override
    public boolean restoreComment(Integer id, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Lấy thông tin bình luận
        ProductCommentEntity comment = productCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));

        // Kiểm tra quyền khôi phục (chỉ chủ sở hữu mới được khôi phục)
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền khôi phục bình luận này");
        }

        // Khôi phục bình luận
        comment.setActive(true);
        comment.setUpdatedAt(LocalDateTime.now());
        productCommentRepository.save(comment);
        return true;
    }

    @Override
    public boolean permanentDeleteComment(Integer id, String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
                
        ProductCommentEntity comment = productCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));
                
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền xóa vĩnh viễn bình luận này");
        }
        
        productCommentRepository.delete(comment);
        return true;
    }
    
    /**
     * Chuyển đổi từ entity sang model
     * @param entity Entity cần chuyển đổi
     * @return Model
     */
    private ProductCommentModel mapToModel(ProductCommentEntity entity) {
        ProductCommentModel model = new ProductCommentModel();
        model.setId(entity.getId());
        model.setProductId(entity.getProduct().getId());
        model.setUserId(entity.getUser().getId());
        model.setComment(entity.getComment());
        model.setActive(entity.isActive());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUsername(entity.getUser().getUsername());
        model.setProductName(entity.getProduct().getName());
        return model;
    }
} 