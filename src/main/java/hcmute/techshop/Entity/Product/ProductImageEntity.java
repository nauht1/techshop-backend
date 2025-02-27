package hcmute.techshop.Entity.Product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageUrl;
    private String altText;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product; // 1 Product có nhiều Images
}
