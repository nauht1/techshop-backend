package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductCommentEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCommentRepository extends JpaRepository<ProductCommentEntity, Integer> {
    
    List<ProductCommentEntity> findByProduct(ProductEntity product);
    
    Optional<ProductCommentEntity> findByUserAndProductAndIsActiveTrue(UserEntity user, ProductEntity product);
    
    List<ProductCommentEntity> findByUserAndIsActiveTrue(UserEntity user);
} 