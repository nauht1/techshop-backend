package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.BrandEntity;
import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Product.BrandModel;
import hcmute.techshop.Service.Product.BrandService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BrandEntity>> createBrand(@RequestBody BrandModel brand) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Thêm thương hiệu thành công", brandService.createBrand(brand))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandEntity>> getBrandById(@PathVariable Integer id) {
        BrandEntity brand = brandService.getBrandById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Lấy thương hiệu thành công", brand)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandEntity>>> getAllBrands() {
        List<BrandEntity> brands = brandService.getAllBrands();
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Lấy danh sách thương hiệu thành công", brands)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BrandEntity>> updateBrand(@PathVariable Integer id, @RequestBody BrandModel brand) {
        brand.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Cập nhật thương hiệu thành công", brandService.updateBrand(brand))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Xóa thương hiệu thành công", "Brand ID " + id + " đã bị xóa")
        );
    }
}
