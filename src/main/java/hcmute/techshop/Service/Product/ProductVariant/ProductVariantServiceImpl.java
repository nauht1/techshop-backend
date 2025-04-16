package hcmute.techshop.Service.Product.ProductVariant;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantAddNewRequestModel;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseModel;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantResponseProjection;
import hcmute.techshop.Model.Product.ProductVariant.ProductVariantUpdateRequestModel;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Product.ProductVariantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements IProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<ProductVariantResponseProjection> getAll() {
        return productVariantRepository.findAllProject();
    }

    @Override
    public ProductVariantResponseProjection getById(Integer id) {
        ProductVariantResponseProjection p =  productVariantRepository.findProjectById(id);
        if (p == null) {
            throw new ResourceNotFoundException("Product variant not found");
        }
        return p;
    }

    @Override
    public List<ProductVariantResponseProjection> getByProductId (Integer productId) {
        return productVariantRepository.findAllProjectByProductId(productId);
    }

    @Override
    public ProductVariantResponseModel addProductVariant(ProductVariantAddNewRequestModel productVariant) {
        if (productVariant.getProductId() == null) {
            throw new IllegalArgumentException("Yêu cầu nhập thông tin product_id");
        } else {
            productRepository.findById(productVariant.getProductId())
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
                .product(productRepository.getReferenceById(productVariant.getProductId()))
                .variantName(productVariant.getVariantName())
                .sku(productVariant.getSku())
                .price(productVariant.getPrice())
                .stock(productVariant.getStock())
                .build();
        return modelMapper.map(productVariantRepository.save(productVariantEntity), ProductVariantResponseModel.class);
    }

    @Override
    public ProductVariantResponseModel updateProductVariant(Integer id, ProductVariantUpdateRequestModel productVariant) {
        Optional<ProductVariantEntity> existingVariant = productVariantRepository.findById(id);
        if (existingVariant.isPresent()) {
            ProductVariantEntity updatedVariant = existingVariant.get();
            updatedVariant.setVariantName(productVariant.getVariantName()); // Cập nhật các thuộc tính cần thiết
            updatedVariant.setPrice(productVariant.getPrice());
            updatedVariant.setStock(productVariant.getStock());
            updatedVariant.setSku(productVariant.getSku());
            return modelMapper.map(productVariantRepository.save(updatedVariant), ProductVariantResponseModel.class);
        }
        throw new ResourceNotFoundException("Product variant not found");
    }

    @Override
    public ProductVariantResponseModel deleteProductVariant(Integer id) {
        ProductVariantEntity deletingEntity = productVariantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product variant not found"));

        productVariantRepository.delete(deletingEntity);
        return modelMapper.map(deletingEntity, ProductVariantResponseModel.class);
    }
}
