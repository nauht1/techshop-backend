package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseProjection;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.ProductVariant.IProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/product-variant")
@RequiredArgsConstructor
public class ProductVariantController {
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
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.ok(ResponseModel.builder().success(false).message(e.getMessage()).build());
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseModel> getProductVariantById(@PathVariable Integer id) {
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
}
