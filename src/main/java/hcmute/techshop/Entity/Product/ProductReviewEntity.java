package hcmute.techshop.Entity.Product;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductEntity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "product_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ProductEntity product;

    private int rating;
    private String comment;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
