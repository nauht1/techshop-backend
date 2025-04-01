package hcmute.techshop.Service.Product.impl;

import java.util.ArrayList;
import java.util.List;

import hcmute.techshop.Enum.PaymentStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hcmute.techshop.Entity.Product.BrandEntity;
import hcmute.techshop.Entity.Product.CategoryEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Repository.Product.BrandRepository;
import hcmute.techshop.Repository.Product.CategoryRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Service.Product.IProductService;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductEntity createProduct(ProductModel request) {
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        BrandEntity brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));

        return productRepository.save(
            new ProductEntity(
                request.getId(),
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getSalePrice(),
                request.getStock(),
                request.isActive(),
                category,
                brand
            )
        );
    }

    @Override
    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductModel> getTop10Products() {
        Pageable pageable  = PageRequest.of(0, 10);
        List<ProductModel> ret = new ArrayList<ProductModel>();
        List<ProductEntity> l = productRepository.findTopSellingProducts(PaymentStatus.SUCCESS, pageable);
        for (ProductEntity e : l) {
            ProductModel p = modelMapper.map(e, ProductModel.class);
            ret.add(p);
        }
        return ret;
    }

    @Override
    public ProductEntity updateProduct(ProductModel request) {
        ProductEntity existingProduct = getProductById(request.getId());

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        BrandEntity brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));

        existingProduct.setName(request.getName());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setSalePrice(request.getSalePrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setActive(request.isActive());
        existingProduct.setCategory(category);
        existingProduct.setBrand(brand);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Integer id) {
        ProductEntity product = getProductById(id);
        productRepository.delete(product);
    }
}
