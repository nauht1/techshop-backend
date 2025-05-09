package hcmute.techshop.Repository.Payment;

import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Enum.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    // Find payments by status
    List<PaymentEntity> findByStatus(PaymentStatus status);

    // Find payment by order ID
    PaymentEntity findByOrder_Id(Integer orderId);
}
