package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Service.Product.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductEntity>> createProduct(@RequestBody ProductModel product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Thêm sản phẩm thành công", productService.createProduct(product))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductEntity>> getProductById(@PathVariable Integer id) {
        ProductEntity product = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Lấy sản phẩm thành công", product)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductEntity>>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Lấy danh sách sản phẩm thành công", products)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductEntity>> updateProduct(@PathVariable Integer id, @RequestBody ProductModel product) {
        product.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Cập nhật sản phẩm thành công", productService.updateProduct(product))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Xóa sản phẩm thành công", "Product ID " + id + " đã bị xóa")
        );
    }
}
