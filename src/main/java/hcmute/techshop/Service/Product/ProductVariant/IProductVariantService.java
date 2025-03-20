package hcmute.techshop.Service.Product.ProductVariant;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Model.Product.ProductVariantModel;

import java.util.List;

public interface IProductVariantService {
    List<ProductVariantEntity> getAll();
    ProductVariantEntity getById(long id);
    List<ProductVariantEntity> getByProductId(int productId);
    ProductVariantEntity addProductVariant(ProductVariantModel productVariant);
    ProductVariantEntity updateProductVariant(Long id, ProductVariantModel productVariant);
    void deleteProductVariant(Long id);
}