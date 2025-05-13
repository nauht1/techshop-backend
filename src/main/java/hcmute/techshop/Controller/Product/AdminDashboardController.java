package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.Auth.UserModel;
import hcmute.techshop.Model.Dashboard.TopUserDTO;
import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Service.Product.IProductService;
import hcmute.techshop.Service.Product.ProductAttribute.IProductAttribute;
import hcmute.techshop.Service.Product.dashboard.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
@RestController
public class AdminDashboardController {
    private final IProductService productService;
    private final IDashboardService dashboardService;

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

    @GetMapping("top-users")
    public ResponseEntity<ResponseModel> findTop10Users(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        PageResponse<TopUserDTO> userModels = dashboardService.getTopUsers(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Get top 10 user successfully", userModels)
        );
    }
}
