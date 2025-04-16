package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Service.Product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class ProductController {

    @RestController
    @RequestMapping("/api/v1/product")
    public static class UserProductController {
        @Autowired
        private IProductService productService;

        @GetMapping("/{id}")
        public ResponseEntity<ResponseModel> getProductById(@PathVariable Integer id) {
            ProductModel product = productService.getProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseModel(true, "Lấy sản phẩm thành công", product)
            );
        }

        @GetMapping
        public ResponseEntity<ResponseModel> getAllProducts() {
            List<ProductModel> products = productService.getAllActiveProducts();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseModel(true, "Lấy danh sách sản phẩm thành công", products)
            );
        }
    }

    @RestController
    @RequestMapping("/api/v1/admin/product")
    public static class AdminProductController {
        @Autowired
        private IProductService productService;

        @GetMapping
        public ResponseEntity<ResponseModel> getAllProducts() {
            List<ProductModel> products = productService.getAllProducts();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseModel(true, "Lấy danh sách sản phẩm thành công", products)
            );
        }

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> createProduct(@RequestBody ProductModel product) {
            System.out.print("Name: " + product.getName() + " " + " category: " +product.getCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseModel(true, "Thêm sản phẩm thành công", productService.createProduct(product))
            );
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> updateProduct(@PathVariable Integer id, @RequestBody ProductModel product) {
            product.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Cập nhật sản phẩm thành công", productService.updateProduct(id, product))
            );
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> deleteProduct(@PathVariable Integer id) {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Xóa sản phẩm thành công", "Product ID " + id + " đã bị xóa")
            );
        }

        @PutMapping("/{id}/soft-delete")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> softDeleteProduct(@PathVariable Integer id) {
            productService.softDeleteProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Đã xóa tạm thời sản phẩm thành công", "Product ID " + id + " đã bị xóa tạm thời")
            );
        }

        // Restore product endpoint
        @PutMapping("/{id}/restore")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> restoreProduct(@PathVariable Integer id) {
            productService.restoreProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Đã phục hồi sản phẩm thành công", "Product ID " + id + " đã được phục hồi")
            );
        }
    }
}