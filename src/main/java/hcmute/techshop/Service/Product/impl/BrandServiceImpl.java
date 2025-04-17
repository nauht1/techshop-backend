package hcmute.techshop.Service.Product.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hcmute.techshop.Entity.Product.BrandEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Model.Product.BrandModel;
import hcmute.techshop.Model.Product.ProductModel;
import hcmute.techshop.Repository.Product.BrandRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Service.Product.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BrandModel createBrand(BrandModel request) {
        BrandEntity entity = new BrandEntity();
        entity.setName(request.getName());
        entity.setCountry(request.getCountry());
        entity.setBrandImg(request.getBrandImg());
        entity.setActive(true);
        entity.setProducts(getProducts(request.getProducts()));
        BrandEntity saved = brandRepository.save(entity);
        return modelMapper.map(saved, BrandModel.class);
    }

    @Override
    public BrandModel getBrandById(Integer id) {
        BrandEntity brand = brandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        return modelMapper.map(brand, BrandModel.class);
    }

    @Override
    public List<BrandModel> getAllBrands() {
        return brandRepository.findAll().stream()
            .map(entity -> modelMapper.map(entity, BrandModel.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<BrandModel> getAllActiveBrands() {
        return brandRepository.findAllByIsActiveTrue().stream()
                .map(product -> modelMapper.map(product, BrandModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public BrandModel updateBrand(BrandModel request) {
        BrandEntity existing = brandRepository.findById(request.getId())
            .orElseThrow(() -> new RuntimeException("Brand not found with id: " + request.getId()));

        existing.setName(request.getName());
        existing.setCountry(request.getCountry());
        existing.setBrandImg(request.getBrandImg());
        existing.setProducts(getProducts(request.getProducts()));

        BrandEntity updated = brandRepository.save(existing);
        return modelMapper.map(updated, BrandModel.class);
    }

    @Override
    public void deleteBrand(Integer id) {
        BrandEntity brand = brandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        brandRepository.delete(brand);
    }

    @Override
    public void softDeleteBrand(Integer id) {
        BrandEntity brand = brandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        brand.setActive(false);
        brandRepository.save(brand);
    }

    @Override
    public void restoreBrand(Integer id) {
        BrandEntity brand = brandRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Brand not found with id: " + id));
        brand.setActive(true);
        brandRepository.save(brand);
    }

    private List<ProductEntity> getProducts(List<ProductModel> products) {
        if (products == null || products.isEmpty()) {
            return List.of();
        }
        List<Integer> ids = products.stream()
            .map(ProductModel::getId)
            .collect(Collectors.toList());
        return productRepository.findAllById(ids);
    }
}
