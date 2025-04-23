package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductVariantModel {
    private Integer id;
    private String sku;
    private String variantName;
    private Double price;
    private Integer stock;
}
