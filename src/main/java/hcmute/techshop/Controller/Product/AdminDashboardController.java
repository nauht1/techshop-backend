package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.IProductService;
import hcmute.techshop.Service.Product.ProductAttribute.IProductAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
@RestController
public class AdminDashboardController {
    private final IProductService productService;

    @GetMapping("top-10-products")
    public ResponseEntity<ResponseModel> findTop10Products() {
        List<ProductModel> ret = productService.getTop10Products();

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .success(true)
                        .message("Get top 10 product successfully")
                        .body(ret)
                        .build()
        );
    }
}
