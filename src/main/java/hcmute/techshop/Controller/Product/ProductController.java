package hcmute.techshop.Controller.Product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Model.Product.*;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.IProductService;
import hcmute.techshop.Service.Tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ProductController {

    @RestController
    @RequestMapping("/api/v1/product")
    public static class UserProductController {
        @Autowired
        private IProductService productService;
        @Autowired
        private TrackingService trackingService;

        private UserEntity ChecAuthenticationForCurrentUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            return (UserEntity) authentication.getPrincipal();
        }

        @GetMapping("/{id}")
        public ResponseEntity<ResponseModel> getProductById(@PathVariable Integer id) {
            ProductModel product = productService.getProductById(id);
            UserEntity user = ChecAuthenticationForCurrentUser();
            if(user != null) {
                trackingService.track(user, EventType.VIEW_PRODUCT, "Product id: " + id.toString());
            }
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

        @GetMapping("/search")
        public ResponseEntity<ResponseModel> searchProduct(@RequestBody SearchProductReq request,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
            PageResponse<ProductModel> products = productService.searchProducts(request.getKeyword(), page, size);
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

        @PutMapping("/image/{id}")
        public ResponseEntity<ResponseModel> updateProductImage(@PathVariable Integer id,
                                                                MultipartFile image) {
            try {
                String newImageUrl = productService.updateProductImage(id, image);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseModel(true, "Đã cập nhật ảnh thành công", newImageUrl));
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel(true, "Something error", null));
            }
        }

        @GetMapping("top-10-products")
        public ResponseEntity<ResponseModel> findTop10Products() {
            List<ProductModel> ret = productService.getTop10Products();

            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get top 10 product successfully")
                            .body(ret)
                            .build()
            );
        }
    }
}