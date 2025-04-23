package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Integer> {
    List<ProductImageEntity> findByProductId(Integer productId);
}
