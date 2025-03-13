package hcmute.techshop.Repository.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findByUserId(Integer userId);

    List<OrderEntity> findByUserAndStatusAndIsActive(UserEntity user, String đãHoànThành, boolean b);
}