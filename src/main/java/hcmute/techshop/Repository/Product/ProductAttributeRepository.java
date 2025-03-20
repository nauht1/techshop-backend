package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Product.ProductAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttributeEntity, Long> {
    public List<ProductAttributeEntity> findByProductId(long id);
}
