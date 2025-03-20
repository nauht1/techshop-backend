package hcmute.techshop.Service.Product.ProductAttribute;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Model.Product.ProductAttributeModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductAttribute {
    List<ProductAttributeEntity> GetAllProductAttribute();
    ProductAttributeEntity getById(long id);
    List<ProductAttributeEntity> getByProductId(long id);
    ProductAttributeEntity save(ProductAttributeModel productAttributeEntity);
    ProductAttributeEntity update(ProductAttributeModel productAttributeEntity);
    ProductAttributeEntity delete(ProductAttributeModel productAttributeEntity);
}
