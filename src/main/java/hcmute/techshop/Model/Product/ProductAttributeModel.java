package hcmute.techshop.Model.Product;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeModel {
    @Nullable
    Integer id;

    String attName;
    String attValue;

    Integer product;
}
