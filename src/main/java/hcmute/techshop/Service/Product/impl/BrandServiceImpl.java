package hcmute.techshop.Service.Product.impl;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public BrandEntity createBrand(BrandModel request) {
        return brandRepository.save(
            new BrandEntity(
                request.getId(),
                request.getName(),
                request.getCountry(),
                request.getBrandImg(),
                request.isActive(),
                getProducts(request.getProducts())
            )
        );
    }

    @Override
    public BrandEntity getBrandById(Integer id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BrandEntity not found with id: " + id));
    }

    @Override
    public List<BrandEntity> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public BrandEntity updateBrand(BrandModel request) {
        BrandEntity existingBrand = getBrandById(request.getId());
        existingBrand.setName(request.getName());
        existingBrand.setCountry(request.getCountry());
        existingBrand.setBrandImg(request.getBrandImg());
        existingBrand.setActive(request.isActive());
        existingBrand.setProducts(getProducts(request.getProducts()));
        return brandRepository.save(existingBrand);
    }

    @Override
    public void deleteBrand(Integer id) {
        BrandEntity brand = getBrandById(id);
        brandRepository.delete(brand);
    }

    private List<ProductEntity> getProducts(List<ProductModel> products) {
        List<Integer> ids = products.stream()
            .map(ProductModel::getId)
            .collect(Collectors.toList());
        return productRepository.findAllById(ids);
    }
}
