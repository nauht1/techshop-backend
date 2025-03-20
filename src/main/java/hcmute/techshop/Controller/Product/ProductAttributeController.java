package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
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
    public ResponseEntity<ResponseModel> findAll(@RequestParam int productId) {
        try {
            List<ProductAttributeEntity> productAttributeEntities = productAttributeService.GetAllProductAttribute();
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get Product Attribues successfully")
                            .body(productAttributeEntities)
                            .build()
            );
        }catch (IllegalArgumentException | ResourceNotFoundException e){
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseModel> findById(@PathVariable("id") Long productId) {
        try {
            List<ProductAttributeEntity> ret = productAttributeService.getByProductId(productId);
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
