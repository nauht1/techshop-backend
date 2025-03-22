package hcmute.techshop.Service.Product.ProductAttribute;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Exception.IllegalArgumentException;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeAddNewRequestModel;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseModel;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeResponseProjection;
import hcmute.techshop.Model.Product.ProductAttribute.ProductAttributeUpadteRequestModel;
import hcmute.techshop.Repository.Product.ProductAttributeRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAttributeServiceImpl implements IProductAttribute {
    private final ProductAttributeRepository repository;
    private final ProductRepository productRepository;
    private final ModelMapper productAttributeMapper;

    @Override
    public List<ProductAttributeResponseProjection>  getAll() {
        return repository.findAllProject();
    }

    @Override
    public ProductAttributeResponseProjection getById(Integer id) {
        ProductAttributeResponseProjection p = repository.findProjectById(id);
        if (p == null) {
            throw new ResourceNotFoundException("Product attribute with id " + id + " not found");
        }
        return p;
    }

    @Override
    public List<ProductAttributeResponseProjection> getByProductId(Integer id) {
        return repository.findAllProjectByProductId(id);
    }

    @Override
    @Transactional
    public ProductAttributeResponseProjection save(ProductAttributeAddNewRequestModel model) {
        ProductEntity product = productRepository.findById(model.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductAttributeEntity newEntity = ProductAttributeEntity.builder()
                .attName(model.getAttName())
                .attValue(model.getAttValue())
                .product(product)
                .build();

        ProductAttributeEntity savedEntity = repository.save(newEntity);

        // Sử dụng mapper để chuyển đổi entity sang DTO
        return productAttributeMapper.map(newEntity, ProductAttributeResponseProjection.class);
    }


    @Override
    public ProductAttributeResponseModel update(ProductAttributeUpadteRequestModel model) {
        if (model.getId()>0) {
            throw new IllegalArgumentException("Product attribute not found");
        }

        ProductAttributeEntity updateEntity = repository.findById(model.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product attribute not found"));

        if (model.getAttName() != null && !model.getAttName().isBlank()) {
            updateEntity.setAttName(model.getAttName());
        }
        if (model.getAttValue() != null && !model.getAttValue().isBlank()) {
            updateEntity.setAttValue(model.getAttValue());
        }
        if (model.getProductId() >0) {
            ProductEntity product = productRepository.findById(model.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            updateEntity.setProduct(product);
        }

        return productAttributeMapper.map(repository.save(updateEntity),ProductAttributeResponseModel.class);
    }

    @Override
    public ProductAttributeResponseModel delete(Integer id) {
        ProductAttributeEntity deleteEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product attribute not found"));
        repository.delete(deleteEntity);
        return productAttributeMapper.map(deleteEntity, ProductAttributeResponseModel.class);
    }
}
