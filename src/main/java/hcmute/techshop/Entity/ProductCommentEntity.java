package hcmute.techshop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product; // 1 Product có nhiều Comments

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 1 User có nhiều Comments
}
