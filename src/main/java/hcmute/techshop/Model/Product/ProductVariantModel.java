package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantModel {
    Integer id;
    Integer product_id;
    String variantName;
    String sku;
    Double price;
    Integer stock;
}
