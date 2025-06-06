package hcmute.techshop.Service.Product.ProductAttribute;

import java.util.List;

import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeAddNewRequestModel;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseModel;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseProjection;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeUpadteRequestModel;

public interface IProductAttribute {
    List<ProductAttributeResponseProjection> getAll();
    ProductAttributeResponseProjection getById(Integer id);
    List<ProductAttributeResponseProjection> getByProductId(Integer id);
    ProductAttributeResponseModel save(ProductAttributeAddNewRequestModel model);
    ProductAttributeResponseModel update(Integer id, ProductAttributeUpadteRequestModel model);
    ProductAttributeResponseModel delete(Integer id);
}
