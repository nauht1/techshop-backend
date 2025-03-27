package hcmute.techshop.Service.Product.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryModel createCategory(CategoryModel request) {
        CategoryEntity savedCategory = categoryRepository.save(
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
        return modelMapper.map(savedCategory, CategoryModel.class);
    }

    @Override
    public CategoryModel getCategoryById(Integer id) {
        return modelMapper.map(
            categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id))
            , CategoryModel.class
        );
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream().map(category -> modelMapper.map(category, CategoryModel.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryModel updateCategory(CategoryModel request) {
        CategoryEntity existingCategory = categoryRepository.findById(request.getId())
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getId()));
        existingCategory.setName(request.getName());
        existingCategory.setSubCategories(getSubcategories(request.getSubCategories()));
        existingCategory.setProducts(getProducts(request.getProducts()));
        return modelMapper.map(
            categoryRepository.save(existingCategory),
            CategoryModel.class
        );
    }

    @Override
    public void deleteCategory(Integer id) {
        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    @Override
    public void softDeleteCategory(Integer id) {
        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    public void restoreCategory(Integer id) {
        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setActive(true);
        categoryRepository.save(category);
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
