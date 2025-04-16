package hcmute.techshop.Model.Product;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BrandModel {
    private Integer id;
    private String name;
    private String country;
    private String brandImg;
    private boolean isActive;
    private List<ProductModel> products;
}