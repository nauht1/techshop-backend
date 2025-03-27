package hcmute.techshop.Controller.Product;

import hcmute.techshop.Model.ResponseModel;
import hcmute.techshop.Model.Product.CategoryModel;
import hcmute.techshop.Service.Product.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class CategoryController {

    @Autowired
    protected CategoryService categoryService;

    @RestController
    @RequestMapping("/api/v1/category")
    public static class UserCategoryController {
        @Autowired
        private CategoryService categoryService;

        @GetMapping("/{id}")
        public ResponseEntity<ResponseModel> getCategoryById(@PathVariable Integer id) {
            CategoryModel category = categoryService.getCategoryById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Lấy danh mục thành công", category)
            );
        }

        @GetMapping
        public ResponseEntity<ResponseModel> getAllCategories() {
            List<CategoryModel> categories = categoryService.getAllCategories();
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Lấy danh sách danh mục thành công", categories)
            );
        }
    }

    @RestController
    @RequestMapping("/api/v1/admin/category")
    public static class AdminCategoryController {
        @Autowired
        private CategoryService categoryService;

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> createCategory(@RequestBody CategoryModel category) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseModel(true, "Thêm danh mục thành công", categoryService.createCategory(category))
            );
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> updateCategory(@PathVariable Integer id, @RequestBody CategoryModel category) {
            category.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Cập nhật danh mục thành công", categoryService.updateCategory(category))
            );
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> deleteCategory(@PathVariable Integer id) {
            categoryService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Xóa danh mục thành công", "Category ID " + id + " đã bị xóa")
            );
        }

        @PutMapping("/{id}/soft-delete")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> softDeleteCategory(@PathVariable Integer id) {
            categoryService.softDeleteCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Đã xóa tạm thời danh mục thành công", "Category ID " + id + " đã bị xóa tạm thời")
            );
        }

        // Restore category endpoint
        @PutMapping("/{id}/restore")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ResponseModel> restoreCategory(@PathVariable Integer id) {
            categoryService.restoreCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseModel(true, "Đã phục hồi danh mục thành công", "Category ID " + id + " đã được phục hồi")
            );
        }
    }
}