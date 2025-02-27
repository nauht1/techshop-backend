package hcmute.techshop.Entity.Product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_attributes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String attName;
    private String attValue;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product; // 1 Product có nhiều Attributes
}
