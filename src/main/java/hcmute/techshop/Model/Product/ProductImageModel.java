package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductImageModel {
    private Integer id;
    private String imageUrl;
    private String altText;
}
