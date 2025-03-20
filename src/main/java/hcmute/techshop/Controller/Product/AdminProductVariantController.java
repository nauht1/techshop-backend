package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductVariantModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductVariant.IProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/product-variant")
@RequiredArgsConstructor
public class AdminProductVariantController {
    @Autowired
    private final IProductVariantService productVariantService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getProductVariants(@RequestParam int productId) {
        try {

            List<ProductVariantEntity> ret = productVariantService.getAll();

            if(productId > 0){
                ret = ret.stream()
                        .filter(productVariantEntity -> productVariantEntity.getProduct().getId() == productId)
                        .collect(Collectors.toList());
            }


            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get product variants successfully")
                            .body(ret)
                            .build()
            );
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getProductVariant(@PathVariable int id) {
        try {
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Get product variant successfully").body(productVariantService.getById(id)).build());
        } catch (ResourceNotFoundException | IllegalArgumentException e ) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseModel> getProductVariant(@PathVariable int id, @RequestParam int productId) {
        try {
            List<ProductVariantEntity> ret = productVariantService.getByProductId(id);

            return ResponseEntity
                    .ok(
                            ResponseModel.builder()
                                    .success(true)
                                    .message("Get product variant successfully")
                                    .body(ret)
                                    .build()
                    );
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
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
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
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
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteProductVariant(@PathVariable Long id) {
        try {
            productVariantService.deleteProductVariant(id);
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Product variant deleted successfully").build());
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }
}
