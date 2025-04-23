package hcmute.techshop.Repository.Order;

import hcmute.techshop.Entity.Product.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer> {
    List<DiscountEntity> findAllByIsActiveTrue();
}
