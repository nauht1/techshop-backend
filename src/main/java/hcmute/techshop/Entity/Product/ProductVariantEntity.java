package hcmute.techshop.Entity.Product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_variants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    private String sku;
    private String variantName;
    private Double price;
    private Integer stock;
    private boolean isActive = true;
}
