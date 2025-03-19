package hcmute.techshop.Service.Product.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.techshop.Entity.Product.CategoryEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.Product.CategoryModel;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Repository.Product.CategoryRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Service.Product.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CategoryEntity createCategory(CategoryModel request) {
        return categoryRepository.save(
            CategoryEntity.builder()
                .id(request.getId())
                .name(request.getName())
                .subCategories(
                    getSubcategories(request.getSubCategories())
                )
                .parentCategory(null)
                .products(
                    getProducts(request.getProducts())
                )
                .isActive(true)
                .build()
        );
    }

    @Override
    public CategoryEntity getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CategoryEntity not found with id: " + id));
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity updateCategory(CategoryModel request) {
        CategoryEntity existingCategory = getCategoryById(request.getId());
        existingCategory.setName(request.getName());
        existingCategory.setSubCategories(getSubcategories(request.getSubCategories()));
        existingCategory.setProducts(getProducts(request.getProducts()));
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        CategoryEntity category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    private List<CategoryEntity> getSubcategories(List<CategoryModel> subcategories) {
        List<Integer> ids = subcategories.stream()
            .map(CategoryModel::getId)
            .collect(Collectors.toList());
        return categoryRepository.findAllById(ids);
    }

    private List<ProductEntity> getProducts(List<ProductModel> products) {
        List<Integer> ids = products.stream()
            .map(ProductModel::getId)
            .collect(Collectors.toList());
        return productRepository.findAllById(ids);
    }
}
