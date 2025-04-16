package hcmute.techshop.Model.Product.ProductAttribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAttributeResponseModel {
    private Integer id;
    private String attName;
    private String attValue;
    private Integer productId;
}
