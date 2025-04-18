package hcmute.techshop.Repository.Order;

import hcmute.techshop.Entity.Order.OrderItemEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Integer> {
    // Tìm các orderItem chưa được đánh giá của một sản phẩm đối với người dùng,
    // chỉ tính các đơn hàng đã giao thành công và đang hoạt động
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.product = ?1 AND oi.order.user = ?2 " +
           "AND oi.order.status = 'DELIVERED' AND oi.order.isActive = true AND oi.isReviewed = false")
    List<OrderItemEntity> findUnreviewedItemsByProductAndUser(ProductEntity product, UserEntity user);
    
    // Tìm các orderItem của người dùng cho sản phẩm,
    // chỉ tính các đơn hàng đã giao thành công và đang hoạt động
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.order.user = ?1 AND oi.product = ?2 " +
           "AND oi.order.status = 'DELIVERED' AND oi.order.isActive = true")
    List<OrderItemEntity> findByUserAndProduct(UserEntity user, ProductEntity product);
}