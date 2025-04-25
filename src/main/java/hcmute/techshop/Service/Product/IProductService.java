package hcmute.techshop.Service.Product;

import java.io.IOException;
import java.util.List;

import hcmute.techshop.Model.Product.CreateProductRequest;
import hcmute.techshop.Model.Product.ProductModel;

public interface IProductService {
    ProductModel createProduct(CreateProductRequest request) throws IOException;
    ProductModel getProductById(Integer id);
    List<ProductModel> getAllProducts();
    List<ProductModel> getAllActiveProducts();
    public List<ProductModel> getTop10Products();
    ProductModel updateProduct(Integer id, CreateProductRequest request) throws IOException;
    void deleteProduct(Integer id);
    void softDeleteProduct(Integer id);
    void restoreProduct(Integer id);
    void deleteProductImage(Integer id);
}
