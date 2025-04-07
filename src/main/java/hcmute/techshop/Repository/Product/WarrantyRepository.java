package hcmute.techshop.Repository.Product;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Product.WarrantyEntity;
import hcmute.techshop.Enum.WarrantyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarrantyRepository extends JpaRepository<WarrantyEntity, Integer> {
    List<WarrantyEntity> findByUser(UserEntity user);
    
    @Query("SELECT w FROM WarrantyEntity w WHERE w.user.email = :email AND w.isActive = true")
    List<WarrantyEntity> findByUserEmail(String email);
    
    List<WarrantyEntity> findByProduct(ProductEntity product);
    List<WarrantyEntity> findByStatus(WarrantyStatus status);
    Page<WarrantyEntity> findByIsActiveTrue(Pageable pageable);
    Page<WarrantyEntity> findByStatusAndIsActiveTrue(WarrantyStatus status, Pageable pageable);
    Optional<WarrantyEntity> findByWcode(String wcode);
    
    @Query("SELECT w FROM WarrantyEntity w WHERE w.isActive = true AND " +
           "(LOWER(w.wcode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(w.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<WarrantyEntity> searchWarranties(String keyword, Pageable pageable);
    
    @Query("SELECT w FROM WarrantyEntity w WHERE w.status = 'ACTIVE' AND w.endDate < :currentDate AND w.isActive = true")
    Page<WarrantyEntity> findExpiredWarranties(LocalDateTime currentDate, Pageable pageable);
    
    @Query("SELECT w FROM WarrantyEntity w WHERE w.status = 'ACTIVE' AND w.endDate BETWEEN :currentDate AND :futureDate AND w.isActive = true")
    Page<WarrantyEntity> findWarrantiesAboutToExpire(LocalDateTime currentDate, LocalDateTime futureDate, Pageable pageable);
    
    List<WarrantyEntity> findByStatusAndEndDateBeforeAndIsActiveTrue(WarrantyStatus status, LocalDateTime endDate);
}