package hcmute.techshop.Controller.Payment;

import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Service.Payment.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    // Get all payments
    @GetMapping
    public ResponseEntity<List<PaymentEntity>> getAllPayments() {
        List<PaymentEntity> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> getPaymentById(@PathVariable Integer id) {
        PaymentEntity payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    // Get payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentEntity>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentEntity> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }

    // Create a new payment
    @PostMapping
    public ResponseEntity<PaymentEntity> createPayment(@RequestBody PaymentEntity payment) {
        PaymentEntity createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    // Update an existing payment
    @PutMapping("/{id}")
    public ResponseEntity<PaymentEntity> updatePayment(@PathVariable Integer id, @RequestBody PaymentEntity payment) {
        PaymentEntity updatedPayment = paymentService.updatePayment(id, payment);
        return ResponseEntity.ok(updatedPayment);
    }

    // Delete a payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
