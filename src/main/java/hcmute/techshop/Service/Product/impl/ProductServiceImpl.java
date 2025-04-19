package hcmute.techshop.Service.Product.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hcmute.techshop.Entity.Product.*;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Model.Product.*;
import hcmute.techshop.Repository.Product.*;
import hcmute.techshop.Service.UploadFile.IUploadFileService;
import hcmute.techshop.Service.UploadFile.UploadFileServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hcmute.techshop.Service.Product.IProductService;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductAttributeRepository productAttributeRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    public ProductModel createProduct(CreateProductRequest request) throws IOException {
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
                .isActive(true)
                .category(category)
                .brand(brand)
                .build();

        // Save and map back to model
        ProductEntity savedProduct = productRepository.save(productEntity);

        // Save images
        if (request.getImages() != null) {
            for (MultipartFile file : request.getImages()) {
                String imageUrl = uploadFileService.uploadImageMultipart(file);
                ProductImageEntity image = new ProductImageEntity();
                image.setProduct(savedProduct);
                image.setImageUrl(imageUrl);
                image.setAltText(savedProduct.getName());
                productImageRepository.save(image);
            }
        }

        // Save attributes
        if (request.getAttributes() != null) {
            for (AttributeDTO attr : request.getAttributes()) {
                ProductAttributeEntity entity = new ProductAttributeEntity();
                entity.setProduct(savedProduct);
                entity.setAttName(attr.getAttName());
                entity.setAttValue(attr.getAttValue());
                productAttributeRepository.save(entity);
            }
        }

        // Save variants
        if (request.getVariants() != null) {
            for (VariantDTO variant : request.getVariants()) {
                ProductVariantEntity entity = new ProductVariantEntity();
                entity.setProduct(savedProduct);
                entity.setSku(variant.getSku());
                entity.setVariantName(variant.getVariantName());
                entity.setPrice(variant.getPrice());
                entity.setStock(variant.getStock());
                productVariantRepository.save(entity);
            }
        }

        return convertToProductModel(savedProduct);
    }

    @Override
    public ProductModel getProductById(Integer id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToProductModel(product);
    }

    @Override
    public List<ProductModel> getAllActiveProducts() {
        return productRepository.findAllByIsActiveTrue().stream()
                .map(this::convertToProductModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToProductModel)
                .collect(Collectors.toList());
    }

    public List<ProductModel> getTop10Products() {
        Pageable pageable  = PageRequest.of(0, 10);
        List<ProductModel> ret = new ArrayList<ProductModel>();
        List<ProductEntity> l = productRepository.findTopSellingProducts(PaymentStatus.SUCCESS, pageable);
        for (ProductEntity e : l) {
//            ProductModel p = modelMapper.map(e, ProductModel.class);
            ret.add(convertToProductModel(e));
        }
        return ret;
    }

    @Override
    public ProductModel updateProduct(Integer id, CreateProductRequest request) throws IOException {
        // Validate product existence
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            existingProduct.setCategory(category);
        }

        if (request.getBrandId() != null) {
            BrandEntity brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getBrandId()));
            existingProduct.setBrand(brand);
        }

        if (request.getName() != null) existingProduct.setName(request.getName());
        if (request.getDescription() != null) existingProduct.setDescription(request.getDescription());
        if (request.getPrice() != null) existingProduct.setPrice(request.getPrice());
        if (request.getSalePrice() != null) existingProduct.setSalePrice(request.getSalePrice());
        if (request.getStock() != null) existingProduct.setStock(request.getStock());

        ProductEntity updatedProduct = productRepository.save(existingProduct);

        if (request.getImages() != null) {
            for (MultipartFile file : request.getImages()) {
                String imageUrl = uploadFileService.uploadImageMultipart(file);
                ProductImageEntity image = new ProductImageEntity();
                image.setProduct(updatedProduct);
                image.setImageUrl(imageUrl);
                image.setAltText(updatedProduct.getName());
                productImageRepository.save(image);
            }
        }

        // Add new attributes if provided
        if (request.getAttributes() != null) {
            for (AttributeDTO attr : request.getAttributes()) {
                ProductAttributeEntity attrEntity = new ProductAttributeEntity();
                attrEntity.setProduct(updatedProduct);
                attrEntity.setAttName(attr.getAttName());
                attrEntity.setAttValue(attr.getAttValue());
                productAttributeRepository.save(attrEntity);
            }
        }

        // Add new variants if provided
        if (request.getVariants() != null) {
            for (VariantDTO variant : request.getVariants()) {
                ProductVariantEntity variantEntity = new ProductVariantEntity();
                variantEntity.setProduct(updatedProduct);
                variantEntity.setSku(variant.getSku());
                variantEntity.setVariantName(variant.getVariantName());
                variantEntity.setPrice(variant.getPrice());
                variantEntity.setStock(variant.getStock());
                productVariantRepository.save(variantEntity);
            }
        }

        return convertToProductModel(updatedProduct);
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

    private ProductModel convertToProductModel(ProductEntity product) {
        // Map basic info
        ProductModel model = modelMapper.map(product, ProductModel.class);
        model.setCategoryId(product.getCategory().getId());
        model.setCategoryName(product.getCategory().getName());
        model.setBrandId(product.getBrand().getId());
        model.setBrandName(product.getBrand().getName());

        // Map images
        model.setImages(productImageRepository.findByProductId(product.getId()).stream().map(image ->
                ProductImageModel.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .altText(image.getAltText())
                        .build()
        ).collect(Collectors.toList()));

        // Map attributes
        model.setAttributes(productAttributeRepository.findByProductId(product.getId()).stream().map(attr ->
                ProductAttributeModel.builder()
                        .id(attr.getId())
                        .attName(attr.getAttName())
                        .attValue(attr.getAttValue())
                        .build()
        ).collect(Collectors.toList()));

        // Map variants
        model.setVariants(productVariantRepository.findByProductId(product.getId()).stream().map(variant ->
                ProductVariantModel.builder()
                        .id(variant.getId())
                        .sku(variant.getSku())
                        .variantName(variant.getVariantName())
                        .price(variant.getPrice())
                        .stock(variant.getStock())
                        .build()
        ).collect(Collectors.toList()));

        return model;
    }

}