package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeAddNewRequestModel;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseModel;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseProjection;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeUpadteRequestModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductAttribute.IProductAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product-attribute")
@RequiredArgsConstructor
public class AdminProductAttributeController {

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
                            .message("Get Product Attributes successfully")
                            .body(productAttributes)
                            .build()
            );
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getProductAttributeById(@PathVariable Integer id) {
        try {
            ProductAttributeResponseProjection attribute = productAttributeService.getById(id);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get product attribute successfully")
                            .body(attribute)
                            .build()
            );
        } catch (hcmute.techshop.Exception.IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
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

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addProductAttribute(@RequestBody ProductAttributeAddNewRequestModel productAttribute) {
        try {
            ProductAttributeResponseModel created = productAttributeService.save(productAttribute);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product attribute added successfully")
                            .body(created)
                            .build()
            );
        } catch (hcmute.techshop.Exception.IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseModel> updateProductAttribute(@PathVariable Integer id, @RequestBody ProductAttributeUpadteRequestModel model) {
        try {
            // Đảm bảo set id từ path variable vào model
            model.setId(id);
            ProductAttributeResponseModel updated = productAttributeService.update(model);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product attribute updated successfully")
                            .body(updated)
                            .build()
            );
        } catch (hcmute.techshop.Exception.IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseModel> deleteProductAttribute(@PathVariable Integer id) {
        try {
            // Vì delete của service nhận ProductAttributeModel, nên khởi tạo model với id cần xóa
            productAttributeService.delete(id);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product attribute deleted successfully")
                            .build()
            );
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
