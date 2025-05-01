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
    private String attName;
    private String attValue;
}
