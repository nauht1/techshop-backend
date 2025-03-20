package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Entity.Product.BrandEntity;
import hcmute.techshop.Model.Product.BrandModel;

public interface BrandService {
    BrandEntity createBrand(BrandModel request);
    BrandEntity getBrandById(Integer id);
    List<BrandEntity> getAllBrands();
    BrandEntity updateBrand(BrandModel request);
    void deleteBrand(Integer id);
}