package hcmute.techshop.Model.Product.ProductAttribute;


import org.springframework.beans.factory.annotation.Value;

public interface ProductAttributeResponseProjection {
    Integer getId();

    String getAttName();
    String getAttValue();

    @Value("#{target.product.id}")
    Integer getProductId();
}
