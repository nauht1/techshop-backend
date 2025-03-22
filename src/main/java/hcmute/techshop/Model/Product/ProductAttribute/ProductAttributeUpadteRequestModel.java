package hcmute.techshop.Model.Product.ProductAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeUpadteRequestModel {
    private Integer id;

    @Nullable
    private String attName;

    @Nullable
    private String attValue;

    private Integer productId;
}
