package hcmute.techshop.Service.Payment;

import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Enum.PaymentStatus;

import java.util.List;

public interface IPaymentService {
    // Retrieve all payments
    List<PaymentEntity> getAllPayments();

    // Retrieve a payment by its ID
    PaymentEntity getPaymentById(Integer id);

    // Retrieve payments by status
    List<PaymentEntity> getPaymentsByStatus(PaymentStatus status);

    // Create a new payment
    PaymentEntity createPayment(PaymentEntity payment);

    // Update an existing payment
    PaymentEntity updatePayment(Integer id, PaymentEntity payment);

    // Delete a payment by its ID
    void deletePayment(Integer id);
}
