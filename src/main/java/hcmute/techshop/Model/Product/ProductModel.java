package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Double salePrice;
    private Integer stock;
    private boolean isActive;
    private Integer categoryId;
    private String categoryName;
    private Integer brandId;
    private String brandName;
    private List<ProductVariantModel> variants;
    private List<ProductAttributeModel> attributes;
    private List<ProductImageModel> images;
}
