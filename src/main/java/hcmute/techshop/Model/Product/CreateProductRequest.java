package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProductRequest {
    private String name;
    private String description;
    private Double price;
    private Double salePrice;
    private Integer stock;
    private Integer categoryId;
    private Integer brandId;

    private List<MultipartFile> images;
    private List<AttributeDTO> attributes;
    private List<VariantDTO> variants;

    // JSON trong form-data
    private String attributesJson;
    private String variantsJson;
}
