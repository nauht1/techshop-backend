package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.Product.ProductModel;

public interface IProductService {
    ProductEntity createProduct(ProductModel request);
    ProductEntity getProductById(Integer id);
    List<ProductEntity> getAllProducts();
    public List<ProductModel> getTop10Products();
    ProductEntity updateProduct(ProductModel request);
    void deleteProduct(Integer id);
}
