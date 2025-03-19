package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductVariant.IProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-variant")
@RequiredArgsConstructor
public class ProductVariantController {
    private final IProductVariantService productVariantService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getProductVariants() {
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel> getProductVariantById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ResponseModel.builder().success(true).message("Get product variant successfully").body(productVariantService.getById(id)).build());
        } catch (Exception e ) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }
}
