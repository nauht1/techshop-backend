package hcmute.techshop.Entity;

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

    private LocalDateTime createdAt;
}
