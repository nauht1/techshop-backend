package hcmute.techshop.Service.Payment;

import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Repository.Payment.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<PaymentEntity> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public PaymentEntity getPaymentById(Integer id) {
        Optional<PaymentEntity> payment = paymentRepository.findById(id);
        return payment.orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
    }

    @Override
    public List<PaymentEntity> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    public PaymentEntity createPayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public PaymentEntity updatePayment(Integer id, PaymentEntity payment) {
        PaymentEntity existingPayment = getPaymentById(id);
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setMethod(payment.getMethod());
        existingPayment.setStatus(payment.getStatus());
        existingPayment.setOrder(payment.getOrder());
        return paymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(Integer id) {
        PaymentEntity payment = getPaymentById(id);
        paymentRepository.delete(payment);
    }
}
