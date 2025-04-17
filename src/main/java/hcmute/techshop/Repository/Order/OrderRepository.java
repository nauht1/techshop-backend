package hcmute.techshop.Repository.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Enum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    Page<OrderEntity> findByUserId(Integer userId, Pageable pageable);
    Page<OrderEntity> findByUserIdAndStatus(Integer userId, OrderStatus status, Pageable pageable);
    List<OrderEntity> findByUserAndStatusAndIsActive(UserEntity user, OrderStatus status, boolean isActive);
}