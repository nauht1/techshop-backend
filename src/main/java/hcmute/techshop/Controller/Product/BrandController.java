package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.Product.BrandModel;
import hcmute.techshop.Service.Product.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class BrandController {

    @Autowired
    protected BrandService brandService;

    @RestController
    @RequestMapping("/api/v1/brand")
    public static class UserBrandController {
        @Autowired
        private BrandService brandService;

        @GetMapping("/{id}")
        public ResponseEntity<ResponseModel> getBrandById(@PathVariable Integer id) {
            BrandModel brand = brandService.getBrandById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Lấy thương hiệu thành công", brand)
            );
        }

        @GetMapping
        public ResponseEntity<ResponseModel> getAllBrands() {
            List<BrandModel> brands = brandService.getAllActiveBrands();
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Lấy danh sách thương hiệu thành công", brands)
            );
        }
    }

    @RestController
    @RequestMapping("/api/v1/admin/brand")
    public static class AdminBrandController {
        @Autowired
        private BrandService brandService;

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> createBrand(@RequestBody BrandModel brand) {
            BrandModel created = brandService.createBrand(brand);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseModel(true, "Thêm thương hiệu thành công", created)
            );
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> updateBrand(
                @PathVariable Integer id,
                @RequestBody BrandModel brand
        ) {
            brand.setId(id);
            BrandModel updated = brandService.updateBrand(brand);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Cập nhật thương hiệu thành công", updated)
            );
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> deleteBrand(@PathVariable Integer id) {
            brandService.deleteBrand(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Xóa thương hiệu thành công", "Brand ID " + id + " đã bị xóa")
            );
        }
    }
}
