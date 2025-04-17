package hcmute.techshop.Service.Product;

import java.util.List;

import hcmute.techshop.Model.Product.BrandModel;

public interface BrandService {
    BrandModel createBrand(BrandModel request);
    BrandModel getBrandById(Integer id);
    List<BrandModel> getAllBrands();
    List<BrandModel> getAllActiveBrands();
    BrandModel updateBrand(BrandModel request);
    void deleteBrand(Integer id);
    void softDeleteBrand(Integer id);
    void restoreBrand(Integer id);
}