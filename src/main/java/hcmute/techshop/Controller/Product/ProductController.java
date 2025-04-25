package hcmute.techshop.Controller.Product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.techshop.Model.Product.AttributeDTO;
import hcmute.techshop.Model.Product.CreateProductRequest;
import hcmute.techshop.Model.Product.VariantDTO;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Service.Product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

        @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> createProduct(@ModelAttribute CreateProductRequest request) throws IOException {
            if (request.getAttributesJson() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<AttributeDTO> attributes = objectMapper.readValue(
                        request.getAttributesJson(), new TypeReference<List<AttributeDTO>>() {});
                request.setAttributes(attributes);
            }

            // Xử lý các đối tượng phức tạp như List<VariantDTO> từ JSON trong form-data
            if (request.getVariantsJson() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<VariantDTO> variants = objectMapper.readValue(
                        request.getVariantsJson(), new TypeReference<List<VariantDTO>>() {});
                request.setVariants(variants);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseModel(true, "Thêm sản phẩm thành công", productService.createProduct(request))
            );
        }

        @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> updateProduct(
                @PathVariable Integer id,
                @ModelAttribute CreateProductRequest request
        ) throws IOException {
            // Xử lý các đối tượng phức tạp như List<AttributeDTO> từ JSON trong form-data
            if (request.getAttributesJson() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<AttributeDTO> attributes = objectMapper.readValue(
                        request.getAttributesJson(), new TypeReference<List<AttributeDTO>>() {});
                request.setAttributes(attributes);
            }

            // Xử lý các đối tượng phức tạp như List<VariantDTO> từ JSON trong form-data
            if (request.getVariantsJson() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<VariantDTO> variants = objectMapper.readValue(
                        request.getVariantsJson(), new TypeReference<List<VariantDTO>>() {});
                request.setVariants(variants);
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseModel(true, "Cập nhật sản phẩm thành công", productService.updateProduct(id, request))
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

        @DeleteMapping("/image/{id}")
        public ResponseEntity<ResponseModel> deleteProductImage(@PathVariable Integer id) {
            try {
                productService.deleteProductImage(id);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseModel(true, "Đã xóa ảnh thành công", null));
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel(true, "Something error", null));
            }
        }
    }
}