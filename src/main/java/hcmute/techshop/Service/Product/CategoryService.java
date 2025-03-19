package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Entity.Product.CategoryEntity;
import hcmute.techshop.Model.Product.CategoryModel;

public interface CategoryService {
    CategoryEntity createCategory(CategoryModel request);
    CategoryEntity getCategoryById(Integer id);
    List<CategoryEntity> getAllCategories();
    CategoryEntity updateCategory(CategoryModel request);
    void deleteCategory(Integer id);
}
