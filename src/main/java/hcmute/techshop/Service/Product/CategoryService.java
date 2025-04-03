package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Model.Product.CategoryModel;

public interface CategoryService {
    CategoryModel createCategory(CategoryModel request);
    CategoryModel getCategoryById(Integer id);
    List<CategoryModel> getAllCategories();
    CategoryModel updateCategory(CategoryModel request);
    void deleteCategory(Integer id);
    void softDeleteCategory(Integer id);
    void restoreCategory(Integer id);
}
