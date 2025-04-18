package hcmute.techshop.Repository.Payment;

import hcmute.techshop.Entity.Payment.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
}
