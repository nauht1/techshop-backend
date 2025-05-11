package hcmute.techshop.Repository.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Enum.OrderStatus;
import hcmute.techshop.Model.Order.OrderStatsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    Page<OrderEntity> findByUserId(Integer userId, Pageable pageable);
    Page<OrderEntity> findByUserIdAndStatus(Integer userId, OrderStatus status, Pageable pageable);
    List<OrderEntity> findByUserAndStatusAndIsActive(UserEntity user, OrderStatus status, boolean isActive);
    Page<OrderEntity> findAll(Pageable pageable);
    Page<OrderEntity> findAllByStatus(Pageable pageable,OrderStatus status);
    List<OrderEntity> findAllByUpdateTimeBetween(LocalDateTime start, LocalDateTime end);
    @Query("SELECT new hcmute.techshop.Model.Order.OrderStatsResponse(" +
            " COUNT(o))" +
//            ", COALESCE(SUM(o.totalPrice), 0)) " +
            "FROM OrderEntity o " +
            "WHERE o.createdAt BETWEEN :startAt AND :endAt")
    OrderStatsResponse getOrderStatsBetween(
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt")   LocalDateTime endAt);
}