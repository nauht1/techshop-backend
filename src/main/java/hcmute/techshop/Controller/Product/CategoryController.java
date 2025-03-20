package hcmute.techshop.Controller.Product;

import hcmute.techshop.Entity.Product.CategoryEntity;
import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Product.CategoryModel;
import hcmute.techshop.Service.Product.CategoryService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryEntity>> createCategory(@RequestBody CategoryModel category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(true, "Thêm danh mục thành công", categoryService.createCategory(category))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryEntity>> getCategoryById(@PathVariable Integer id) {
        CategoryEntity category = categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Lấy danh mục thành công", category)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryEntity>>> getAllCategories() {
        List<CategoryEntity> categories = categoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Lấy danh sách danh mục thành công", categories)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoryEntity>> updateCategory(@PathVariable Integer id, @RequestBody CategoryModel category) {
        category.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Cập nhật danh mục thành công", categoryService.updateCategory(category))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(true, "Xóa danh mục thành công", "Category ID " + id + " đã bị xóa")
        );
    }
}
