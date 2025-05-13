package hcmute.techshop.Repository.Auth;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Integer id);

    @Query("""
        SELECT o.user, SUM(p.amount) as totalAmount
        FROM PaymentEntity p
        JOIN p.order o
        WHERE p.status = :SUCCESS_STATUS
        GROUP BY o.user
    """
    )
    Page<Object[]> findTopUser(@Param("SUCCESS_STATUS") PaymentStatus status, Pageable pageable);
}
