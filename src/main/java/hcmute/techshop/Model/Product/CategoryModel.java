package hcmute.techshop.Model.Product;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryModel {
    private Integer id;
    private String name;
    private List<CategoryModel> subCategories;
    private List<ProductModel> products;
    private boolean isActive;
}
