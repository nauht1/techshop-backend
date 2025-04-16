package hcmute.techshop.Model.Product.ProductVariant;

import org.springframework.beans.factory.annotation.Value;

public interface ProductVariantResponseProjection {
    Integer getId();

    @Value("#{target.product.id}")
    Integer getProductId();

    String getVariantName();
    String getSku();
    Double getPrice();
    Integer getStock();
}
