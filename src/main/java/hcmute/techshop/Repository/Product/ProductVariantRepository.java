package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
    public List<ProductVariantEntity> findByProductId(int productId);
}
