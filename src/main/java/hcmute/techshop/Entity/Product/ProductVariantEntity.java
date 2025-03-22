package hcmute.techshop.Entity.Product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product_variants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @NotNull
    private String sku;
    @NotNull
    private String variantName;
    @NotNull
    private Double price;
    @NotNull
    private Integer stock;
}
