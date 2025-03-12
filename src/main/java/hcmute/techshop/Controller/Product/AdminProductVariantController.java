package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Model.Product.ProductVariantModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductVariant.IProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/product-variant")
@RequiredArgsConstructor
public class AdminProductVariantController {
    @Autowired
    private final IProductVariantService productVariantService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getProducts() {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get product variants successfully")
                            .body(productVariantService.getAll())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addProductVariant(@RequestBody ProductVariantModel productVariant) {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product variant added successfully")
                            .body(productVariantService.addProductVariant(productVariant))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseModel> updateProductVariant(@PathVariable Long id, @RequestBody ProductVariantModel productVariant) {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product variant updated successfully")
                            .body(productVariantService.updateProductVariant(id, productVariant))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteProductVariant(@PathVariable Long id) {
        try {
            productVariantService.deleteProductVariant(id);
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Product variant deleted successfully").build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }
}
