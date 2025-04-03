package hcmute.techshop.Service.Product.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.techshop.Entity.Product.BrandEntity;
import hcmute.techshop.Entity.Product.CategoryEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Repository.Product.BrandRepository;
import hcmute.techshop.Repository.Product.CategoryRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Service.Product.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductModel createProduct(ProductModel request) {
        // Set ID to null for new product
        request.setId(null);

        // Fetch and validate category and brand
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        BrandEntity brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));

        // Create product entity
        ProductEntity productEntity = ProductEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .salePrice(request.getSalePrice())
                .stock(request.getStock())
                .isActive(request.isActive())
                .category(category)
                .brand(brand)
                .build();

        // Save and map back to model
        ProductEntity savedProduct = productRepository.save(productEntity);
        return modelMapper.map(savedProduct, ProductModel.class);
    }

    @Override
    public ProductModel getProductById(Integer id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return modelMapper.map(product, ProductModel.class);
    }

    @Override
    public List<ProductModel> getAllActiveProducts() {
        return productRepository.findAllByIsActiveTrue().stream()
                .map(product -> modelMapper.map(product, ProductModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductModel updateProduct(Integer id ,ProductModel request) {
        // Validate product existence
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getId()));

        // Fetch and validate category and brand
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        BrandEntity brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));

        // Update existing product
        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setSalePrice(request.getSalePrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setActive(request.isActive());
        existingProduct.setCategory(category);
        existingProduct.setBrand(brand);

        // Save and map back to model
        ProductEntity updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductModel.class);
    }

    @Override
    public void deleteProduct(Integer id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public void softDeleteProduct(Integer id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void restoreProduct(Integer id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setActive(true);
        productRepository.save(product);
    }
}