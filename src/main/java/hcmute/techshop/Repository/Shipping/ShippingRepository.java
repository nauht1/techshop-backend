package hcmute.techshop.Repository.Shipping;

import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingMethodEntity, Integer> {
    List<ShippingMethodEntity> findByIsActiveTrue();
}
