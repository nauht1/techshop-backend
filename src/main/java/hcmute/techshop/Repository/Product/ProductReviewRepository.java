package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Product.ProductReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, Integer> {
    
    Optional<ProductReviewEntity> findByUserAndProductAndIsActiveTrue(UserEntity user, ProductEntity product);
    
    List<ProductReviewEntity> findByProduct(ProductEntity product);

    List<ProductReviewEntity> findByProductAndUser(ProductEntity product, UserEntity user);
} 