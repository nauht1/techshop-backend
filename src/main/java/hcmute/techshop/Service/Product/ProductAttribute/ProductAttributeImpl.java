package hcmute.techshop.Service.Product.ProductAttribute;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductAttributeModel;
import hcmute.techshop.Repository.Product.ProductAttributeRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAttributeImpl implements IProductAttribute {
    private final ProductAttributeRepository repository;
    private final ProductRepository productRepository;

    @Override
    public List<ProductAttributeEntity> GetAllProductAttribute() {
        return repository.findAll();
    }

    @Override
    public ProductAttributeEntity getById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product attribute not found"));
    }

    @Override
    public ProductAttributeEntity save(ProductAttributeModel productAttributeModel) {
        ProductEntity product = productRepository.findById(productAttributeModel.getProduct())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductAttributeEntity newEntity = ProductAttributeEntity.builder()
                .attName(productAttributeModel.getAttName())
                .attValue(productAttributeModel.getAttValue())
                .product(product)
                .build();

        return repository.save(newEntity);
    }

    @Override
    public ProductAttributeEntity update(ProductAttributeModel productAttributeModel) {
        if (productAttributeModel.getId() == null) {
            throw new ResourceNotFoundException("Product attribute not found");
        }

        ProductAttributeEntity updateEntity = repository.findById(productAttributeModel.getId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Product attribute not found"));

        if (productAttributeModel.getAttName() != null && !productAttributeModel.getAttName().isBlank()) {
            updateEntity.setAttName(productAttributeModel.getAttName());
        }
        if (productAttributeModel.getAttValue() != null && !productAttributeModel.getAttValue().isBlank()) {
            updateEntity.setAttValue(productAttributeModel.getAttValue());
        }
        if (productAttributeModel.getProduct() != null) {
            ProductEntity product = productRepository.findById(productAttributeModel.getProduct())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            updateEntity.setProduct(product);
        }

        return repository.save(updateEntity);
    }

    @Override
    public ProductAttributeEntity delete(ProductAttributeModel productAttributeModel) {
        if (productAttributeModel.getId() == null) {
            throw new ResourceNotFoundException("Product attribute not found");
        }

        ProductAttributeEntity deleteEntity = repository.findById(productAttributeModel.getId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Product attribute not found"));
        repository.delete(deleteEntity);
        return deleteEntity;
    }
}
