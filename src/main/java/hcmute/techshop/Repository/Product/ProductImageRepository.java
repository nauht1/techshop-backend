package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Integer> {
    List<ProductImageEntity> findByProductId(Integer productId);
    Optional<ProductImageEntity> findFirstByProductId(Integer productId);
}
