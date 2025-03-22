package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseProjection;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Repository.Product.ProductAttributeRepository;
import hcmute.techshop.Service.Product.ProductAttribute.IProductAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-attribute")
public class ProductAttributeController {
    private final IProductAttribute productAttributeService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> findAll(@RequestParam(required = false) Integer productId) {
        try {
            List<ProductAttributeResponseProjection> productAttributes;

            if (productId != null) {
                productAttributes = productAttributeService.getByProductId(productId);
            } else {
                productAttributes = productAttributeService.getAll(); // Hàm lấy tất cả
            }

            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get Product Attribues successfully")
                            .body(productAttributes)
                            .build()
            );
        }catch (IllegalArgumentException | ResourceNotFoundException e){
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseModel> findById(@PathVariable("id") Integer productId) {
        try {
            List<ProductAttributeResponseProjection> ret = productAttributeService.getByProductId(productId);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get Product Attribute successfully")
                            .body(ret)
                            .build()
            );
        }
        catch (IllegalArgumentException | ResourceNotFoundException e){
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }
}
