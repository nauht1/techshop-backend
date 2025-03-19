package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Repository.Product.ProductAttributeRepository;
import hcmute.techshop.Service.Product.ProductAttribute.IProductAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-attribute")
public class ProductAttributeController {
    private final IProductAttribute productAttributeService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> findAll() {
        try {
            List<ProductAttributeEntity> productAttributeEntities = productAttributeService.GetAllProductAttribute();
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Get Product Attribues successfully").body(productAttributeEntities).build());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> findById(@PathVariable("id") Long id) {
        try {
            ProductAttributeEntity ret = productAttributeService.getById(id);
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Get Product Attribute successfully").body(ret).build());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }
}
