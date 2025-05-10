package hcmute.techshop.Service.Product;

import java.io.IOException;
import java.util.List;

import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Model.Product.CreateProductRequest;
import hcmute.techshop.Model.Product.ProductModel;
import org.springframework.web.multipart.MultipartFile;

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
    String updateProductImage(Integer id, MultipartFile image) throws IOException;
    PageResponse<ProductModel> searchProducts(String keyword, int page, int size);
}
