package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Model.Product.ProductAttributeModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductAttribute.IProductAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/product-attribute")
@RequiredArgsConstructor
public class AdminProductAttributeController {

    @Autowired
    private final IProductAttribute productAttributeService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getProductAttributes() {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get product attributes successfully")
                            .body(productAttributeService.GetAllProductAttribute())
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getProductAttributeById(@PathVariable Long id) {
        try {
            ProductAttributeEntity attribute = productAttributeService.getById(id);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Get product attribute successfully")
                            .body(attribute)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseModel> addProductAttribute(@RequestBody ProductAttributeModel productAttribute) {
        try {
            ProductAttributeEntity created = productAttributeService.save(productAttribute);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product attribute added successfully")
                            .body(created)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseModel> updateProductAttribute(@PathVariable Integer id, @RequestBody ProductAttributeModel productAttribute) {
        try {
            // Đảm bảo set id từ path variable vào model
            productAttribute.setId(id);
            ProductAttributeEntity updated = productAttributeService.update(productAttribute);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product attribute updated successfully")
                            .body(updated)
                            .build()
            );
        } catch (Exception e) {
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
            ProductAttributeModel model = new ProductAttributeModel();
            model.setId(id);
            productAttributeService.delete(model);
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(true)
                            .message("Product attribute deleted successfully")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
