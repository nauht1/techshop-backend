package hcmute.techshop.Service.Product.comment;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCommentServiceImpl implements IProductCommentService {

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

                // Lấy thông tin bình luận (không cần lọc theo isActive)
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
        public boolean toggleCommentStatus(Integer id, String email) {
                // Lấy thông tin người dùng
                UserEntity user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

                // Lấy thông tin bình luận
                ProductCommentEntity comment = productCommentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));

                // Kiểm tra quyền cập nhật (chỉ chủ sở hữu mới được cập nhật)
                if (!comment.getUser().getId().equals(user.getId())) {
                        throw new IllegalStateException("Bạn không có quyền thay đổi trạng thái bình luận này");
                }

                // Đảo ngược trạng thái kích hoạt
                comment.setActive(!comment.isActive());
                comment.setUpdatedAt(LocalDateTime.now());
                productCommentRepository.save(comment);

                return comment.isActive();
        }

        //Lấy danh sách bình luận của một sản phẩm của người dùng thường
        @Override
        public List<ProductCommentModel> getProductComments(Integer productId, String email) {
                ProductEntity product = productRepository.findById(productId)
                                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

                UserEntity user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

                List<ProductCommentEntity> comments = productCommentRepository.findByProduct(product);

                return comments.stream()
                                .filter(comment -> comment.isActive()
                                                || comment.getUser().getId().equals(user.getId()))
                                .map(comment -> {
                                        ProductCommentModel model = mapToModel(comment);
                                        model.setOwner(comment.getUser().getId().equals(user.getId()));
                                        return model;
                                })
                                .collect(Collectors.toList());
        }

        @Override
        public List<ProductCommentModel> getAllProductCommentsForAdmin(Integer productId, String adminEmail) {
                ProductEntity product = productRepository.findById(productId)
                                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

                UserEntity admin = userRepository.findByEmail(adminEmail)
                                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin admin"));

                // Kiểm tra xem người dùng có vai trò admin không
                if (!admin.getRole().equals(Role.ROLE_ADMIN)) {
                        throw new IllegalStateException("Bạn không có quyền admin để thực hiện chức năng này");
                }

                List<ProductCommentEntity> comments = productCommentRepository.findByProduct(product);

                return comments.stream()
                                .map(comment -> {
                                        ProductCommentModel model = mapToModel(comment);
                                        model.setOwner(comment.getUser().getId().equals(admin.getId()));
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
                boolean isOwner = comment.getUser().getId().equals(user.getId());

                // Logic quyền truy cập:
                // Admin có thể xem tất cả
                // Người dùng thường chỉ có thể xem bình luận không bị ẩn HOẶC bình luận do
                // chính họ tạo (kể cả khi đã bị ẩn)
                if (!isAdmin && !(comment.isActive() || isOwner)) {
                        throw new IllegalArgumentException("Bạn không có quyền xem bình luận này");
                }

                // Chuyển đổi sang model và trả về
                ProductCommentModel model = mapToModel(comment);
                model.setOwner(isOwner);
                return model;
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

        @Override
        public boolean adminToggleCommentStatus(Integer id, String adminEmail) {
                UserEntity admin = userRepository.findByEmail(adminEmail)
                                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin admin"));

                if (!admin.getRole().equals(Role.ROLE_ADMIN)) {
                        throw new IllegalStateException("Bạn không có quyền admin để thực hiện chức năng này");
                }

                ProductCommentEntity comment = productCommentRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Bình luận không tồn tại"));

                // Đảo ngược trạng thái kích hoạt
                comment.setActive(!comment.isActive());
                comment.setUpdatedAt(LocalDateTime.now());
                productCommentRepository.save(comment);
                return comment.isActive();
        }

        @Override
        public List<ProductCommentModel> getAllCommentsForAdmin(String adminEmail) {
                UserEntity admin = userRepository.findByEmail(adminEmail)
                                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin admin"));

                if (!admin.getRole().equals(Role.ROLE_ADMIN)) {
                        throw new IllegalStateException("Bạn không có quyền admin để thực hiện chức năng này");
                }
                List<ProductCommentEntity> comments = productCommentRepository.findAll();
                if (comments.isEmpty()) {
                    throw new IllegalArgumentException("Danh sách bình luận rỗng");
                }
                return comments.stream()
                                .map(comment -> {
                                        ProductCommentModel model = mapToModel(comment);
                                        model.setOwner(comment.getUser().getId().equals(admin.getId()));
                                        return model;
                        })
                        .collect(Collectors.toList());
        }

        /**
         * Chuyển đổi từ entity sang model
         * 
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