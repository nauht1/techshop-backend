package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Model.Product.ProductModel;

public interface ProductService {
    ProductModel createProduct(ProductModel request);
    ProductModel getProductById(Integer id);
    List<ProductModel> getAllProducts();
    ProductModel updateProduct(ProductModel request);
    void deleteProduct(Integer id);
    void softDeleteProduct(Integer id);
    void restoreProduct(Integer id);
}
