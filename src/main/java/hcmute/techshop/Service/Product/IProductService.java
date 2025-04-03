package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Model.Product.ProductModel;

public interface IProductService {
    ProductModel createProduct(ProductModel request);
    ProductModel getProductById(Integer id);
    List<ProductModel> getAllProducts();
    List<ProductModel> getAllActiveProducts();
    public List<ProductModel> getTop10Products();
    ProductModel updateProduct(Integer Id, ProductModel request);
    void deleteProduct(Integer id);
    void softDeleteProduct(Integer id);
    void restoreProduct(Integer id);
}
