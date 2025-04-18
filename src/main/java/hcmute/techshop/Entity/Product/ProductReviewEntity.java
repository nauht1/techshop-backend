package hcmute.techshop.Entity.Product;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product; // 1 Product có nhiều Reviews

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 1 User có nhiều Reviews
    
    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItemEntity orderItem; // Liên kết với OrderItem cụ thể

    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}