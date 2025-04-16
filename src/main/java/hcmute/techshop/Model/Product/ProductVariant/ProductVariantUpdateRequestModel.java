package hcmute.techshop.Model.Product.ProductVariant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantUpdateRequestModel {
    Integer id;

    @Nullable
    Integer productId;
    @Nullable
    String variantName;
    @Nullable
    String sku;
    @Nullable
    Double price;
    @Nullable
    Integer stock;
}
