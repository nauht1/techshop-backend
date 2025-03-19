package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeEntity, Long> {
}
