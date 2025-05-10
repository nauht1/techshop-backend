package hcmute.techshop.Repository.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Enum.PaymentStatus;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    List<ProductEntity> findAllByIsActiveTrue();

    ///
    ///
    ///
    @Query("SELECT p " + // lấy product entity
            "FROM OrderItemEntity oi " +    //
            "JOIN oi.product p " +          // kết 3 bảng thông qua mối qhe giữa p,oi,o
            "JOIN oi.order o " +            //
            "JOIN PaymentEntity pm ON pm.order.id = o.id " + // kết bảng payment để lấy lượt bán
            "WHERE pm.status = :SUCCESS_STATUS " + // kiểm tra bán thành công hay không
            "GROUP BY p.id " + // nhóm lại theo sản phẩm
            "ORDER BY COUNT(oi.id) DESC") // sắp xếp theo lượt bán giảm dần
    List<ProductEntity> findTopSellingProducts(@Param("SUCCESS_STATUS") PaymentStatus successStatus, Pageable pageable);

    Page<ProductEntity> searchProductsByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}