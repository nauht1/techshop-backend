package hcmute.techshop.Controller.Product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantAddNewRequestModel;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseProjection;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantUpdateRequestModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductVariant.IProductVariantService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/product-variant")
@RequiredArgsConstructor
public class AdminProductVariantController {
    @Autowired
    private final IProductVariantService productVariantService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getProductVariants(@RequestParam(required = false) Integer productId) {
        try {
            List<ProductVariantResponseProjection> ret;
            if (productId != null) {
                ret = productVariantService.getByProductId(productId);
            } else {
                ret = productVariantService.getAll();
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
    public ResponseEntity<ResponseModel> getProductVariant(@PathVariable Integer id, @RequestParam int productId) {
        try {
            List<ProductVariantResponseProjection> ret = productVariantService.getByProductId(id);

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
    public ResponseEntity<ResponseModel> addProductVariant(@RequestBody ProductVariantAddNewRequestModel productVariant) {
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
    public ResponseEntity<ResponseModel> updateProductVariant(@PathVariable Integer id, @RequestBody ProductVariantUpdateRequestModel productVariant) {
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
    public ResponseEntity<ResponseModel> deleteProductVariant(@PathVariable Integer id) {
        try {
            productVariantService.deleteProductVariant(id);
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Product variant deleted successfully").build());
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }
}
