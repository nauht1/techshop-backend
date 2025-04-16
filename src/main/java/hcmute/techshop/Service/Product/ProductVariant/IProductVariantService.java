package hcmute.techshop.Service.Product.ProductVariant;

import java.util.List;

import hcmute.techshop.Model.Product.ProductVariant.ProductVariantAddNewRequestModel;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseModel;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseProjection;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantUpdateRequestModel;

public interface IProductVariantService {
    List<ProductVariantResponseProjection> getAll();
    ProductVariantResponseProjection getById(Integer id);
    List<ProductVariantResponseProjection> getByProductId(Integer productId);
    ProductVariantResponseModel addProductVariant(ProductVariantAddNewRequestModel model);
    ProductVariantResponseModel updateProductVariant(Integer id, ProductVariantUpdateRequestModel model);
    ProductVariantResponseModel deleteProductVariant(Integer id);
}