package hcmute.techshop.Model.Product.ProductVariant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantAddNewRequestModel {
    Integer id;
    Integer productId;
    String variantName;
    String sku;
    Double price;
    Integer stock;
}
