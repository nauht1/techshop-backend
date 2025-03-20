package hcmute.techshop.Service.Product.ProductVariant;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductVariantModel;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Product.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hcmute.techshop.Exception.IllegalArgumentException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements IProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ProductVariantEntity> getAll() {
        return productVariantRepository.findAll();
    }

    @Override
    public ProductVariantEntity getById(long id) {
        return productVariantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));
    }

    @Override
    public List<ProductVariantEntity> getByProductId (int productId) {
        return productVariantRepository.findByProductId(productId);
    }

    @Override
    public ProductVariantEntity addProductVariant(ProductVariantModel productVariant) {
        if (productVariant.getProduct_id() == null) {
            throw new IllegalArgumentException("Yêu cầu nhập thông tin product_id");
        } else {
            productRepository.findById(productVariant.getProduct_id())
                    .orElseThrow(() -> new IllegalArgumentException("Yêu cầu nhập thông tin product_id hợp lệ"));
        }
        if (productVariant.getVariantName() == null || productVariant.getVariantName().isEmpty()) {
            throw new IllegalArgumentException("Yêu cầu nhập thông tin variantName");
        }
        if (productVariant.getSku() == null || productVariant.getSku().isEmpty()) {
            throw new IllegalArgumentException("Yêu cầu nhập thông tin sku");
        }
        if (productVariant.getPrice() == null) {
            throw new IllegalArgumentException("Yêu cầu nhập thông tin price");
        }
        if (productVariant.getStock() == null) {
            throw new IllegalArgumentException("Yêu cầu nhập thông tin stock");
        }

        ProductVariantEntity productVariantEntity = ProductVariantEntity.builder()
                .id(productVariant.getId())
                .product(productRepository.getReferenceById(productVariant.getProduct_id()))
                .variantName(productVariant.getVariantName())
                .sku(productVariant.getSku())
                .price(productVariant.getPrice())
                .stock(productVariant.getStock())
                .build();
        return productVariantRepository.save(productVariantEntity);
    }

    @Override
    public ProductVariantEntity updateProductVariant(Long id, ProductVariantModel productVariant) {
        Optional<ProductVariantEntity> existingVariant = productVariantRepository.findById(id);
        if (existingVariant.isPresent()) {
            ProductVariantEntity updatedVariant = existingVariant.get();
            updatedVariant.setVariantName(productVariant.getVariantName()); // Cập nhật các thuộc tính cần thiết
            updatedVariant.setPrice(productVariant.getPrice());
            updatedVariant.setStock(productVariant.getStock());
            return productVariantRepository.save(updatedVariant);
        }
        throw new ResourceNotFoundException("Product variant not found");
    }

    @Override
    public void deleteProductVariant(Long id) {
        productVariantRepository.deleteById(id);
    }
}
