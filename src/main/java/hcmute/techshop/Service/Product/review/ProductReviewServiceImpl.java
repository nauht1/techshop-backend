package hcmute.techshop.Service.Product.review;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderItemEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Product.ProductReviewEntity;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Enum.Role;
import hcmute.techshop.Model.Product.AddReviewRequest;
import hcmute.techshop.Model.Product.ProductReviewModel;
import hcmute.techshop.Model.Product.UpdateReviewRequest;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Order.OrderItemRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Product.ProductReviewRepository;
import hcmute.techshop.Service.Tracking.ITrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements IProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final ITrackingService trackingService;
    @Override
    public ProductReviewModel addReview(AddReviewRequest request, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Lấy thông tin sản phẩm
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        // Kiểm tra xem người dùng đã từng mua sản phẩm này hay chưa
        List<OrderItemEntity> purchasedItems = orderItemRepository.findByUserAndProduct(user, product);
        if (purchasedItems.isEmpty()) {
            throw new IllegalArgumentException("Bạn cần mua sản phẩm này trước khi có thể đánh giá.");
        }
        
        // Lấy các OrderItem của sản phẩm chưa được đánh giá
        List<OrderItemEntity> unreviewedItems = orderItemRepository.findUnreviewedItemsByProductAndUser(product, user);
        if (unreviewedItems.isEmpty()) {
            throw new IllegalArgumentException("Bạn đã đánh giá sản phẩm này rồi.");
        }
        
        // Lấy OrderItem đầu tiên chưa được đánh giá
        OrderItemEntity orderItem = unreviewedItems.get(0);
        
        // Tạo đánh giá mới
        ProductReviewEntity review = new ProductReviewEntity();
        review.setUser(user);
        review.setProduct(product);
        review.setOrderItem(orderItem);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setActive(true);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        // Lưu đánh giá
        review = productReviewRepository.save(review);
        
        // Đánh dấu OrderItem là đã đánh giá
        orderItem.setReviewed(true);
        orderItemRepository.save(orderItem);

        // Chuyển đổi sang model và trả về
        ProductReviewModel model = mapToModel(review);
        model.setOwner(true);
        trackingService.track(user, EventType.WRITE_REVIEW, "ProductId: " + product.getId().toString());
        return model;
    }

    @Override
    public ProductReviewModel updateReview(UpdateReviewRequest request, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Lấy thông tin đánh giá (không cần lọc theo isActive)
        ProductReviewEntity review = productReviewRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá không tồn tại"));

        // Kiểm tra quyền cập nhật (chỉ chủ sở hữu mới được cập nhật)
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền cập nhật đánh giá này");
        }

        // Cập nhật thông tin đánh giá
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        // Lưu vào database
        review = productReviewRepository.save(review);

        // Chuyển đổi sang model và trả về
        ProductReviewModel model = mapToModel(review);
        model.setOwner(true);
        return model;
    }

    @Override
    public boolean toggleReviewStatus(Integer id, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Lấy thông tin đánh giá
        ProductReviewEntity review = productReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá không tồn tại"));

        // Kiểm tra quyền cập nhật (chỉ chủ sở hữu mới được cập nhật)
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền thay đổi trạng thái đánh giá này");
        }

        // Đảo ngược trạng thái kích hoạt
        review.setActive(!review.isActive());
        productReviewRepository.save(review);

        return review.isActive();
    }

    @Override
    public List<ProductReviewModel> getProductReviews(Integer productId, String email) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        List<ProductReviewEntity> reviews = productReviewRepository.findByProduct(product);

        return reviews.stream()
                .filter(review -> review.isActive() || review.getUser().getId().equals(user.getId()))
                .map(review -> {
                    ProductReviewModel model = mapToModel(review);
                    model.setOwner(review.getUser().getId().equals(user.getId()));
                    return model;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewModel> getAllProductReviewsForAdmin(Integer productId, String adminEmail) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));
        
        UserEntity admin = userRepository.findByUsername(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin admin"));
                
        List<ProductReviewEntity> reviews = productReviewRepository.findByProduct(product);
        
        return reviews.stream()
                .map(review -> {
                    ProductReviewModel model = mapToModel(review);
                    model.setOwner(review.getUser().getId().equals(admin.getId()));
                    return model;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductReviewModel getReviewById(Integer id, String email) {
        // Lấy thông tin người dùng
        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        // Lấy thông tin đánh giá
        ProductReviewEntity review = productReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá không tồn tại"));

        // Kiểm tra xem người dùng có phải là Admin không
        boolean isAdmin = user.getRole().equals(Role.ROLE_ADMIN);
        boolean isOwner = review.getUser().getId().equals(user.getId());

        // Logic quyền truy cập:
        // Admin có thể xem tất cả
        // Người dùng thường chỉ có thể xem đánh giá không bị ẩn HOẶC đánh giá do chính
        // họ tạo (kể cả khi đã bị ẩn)
        if (!isAdmin && !(review.isActive() || isOwner)) {
            throw new IllegalArgumentException("Bạn không có quyền xem đánh giá này");
        }

        // Chuyển đổi sang model và trả về
        ProductReviewModel model = mapToModel(review);
        model.setOwner(isOwner);
        return model;
    }

    @Override
    public boolean permanentDeleteReview(Integer id, String email) {
        UserEntity user = userRepository.findByUsername(email)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

        ProductReviewEntity review = productReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá không tồn tại"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Bạn không có quyền xóa vĩnh viễn đánh giá này");
        }

        // Đặt lại trạng thái isReviewed của OrderItem thành false
        OrderItemEntity orderItem = review.getOrderItem();
        orderItem.setReviewed(false);
        orderItemRepository.save(orderItem);

        // Xóa đánh giá
        productReviewRepository.delete(review);
        return true;
    }

    @Override
    public boolean adminToggleReviewStatus(Integer id) {
        ProductReviewEntity review = productReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Đánh giá không tồn tại"));

        // Đảo ngược trạng thái kích hoạt
        review.setActive(!review.isActive());
        productReviewRepository.save(review);
        return review.isActive();
    }

    @Override
    public List<ProductReviewModel> getAllReviewsForAdmin(String adminEmail) {
        UserEntity admin = userRepository.findByUsername(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin admin"));

        List<ProductReviewEntity> reviews = productReviewRepository.findAll();
        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("Danh sách đánh giá rỗng");
        }
        return reviews.stream()
                .map(review -> {
                    ProductReviewModel model = mapToModel(review);
                    model.setOwner(review.getUser().getId().equals(admin.getId()));
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
    private ProductReviewModel mapToModel(ProductReviewEntity entity) {
        ProductReviewModel model = new ProductReviewModel();
        model.setId(entity.getId());
        model.setProductId(entity.getProduct().getId());
        model.setUserId(entity.getUser().getId());
        model.setRating(entity.getRating());
        model.setComment(entity.getComment());
        model.setActive(entity.isActive());
        model.setCreatedAt(entity.getCreatedAt());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUsername(entity.getUser().getUsername());
        model.setProductName(entity.getProduct().getName());
        model.setOrderItemId(entity.getOrderItem().getId());
        return model;
    }
}