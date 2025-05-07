package hcmute.techshop.Model.Payment;

import hcmute.techshop.Enum.PaymentMethod;
import hcmute.techshop.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel {
    private Integer id;
    private Integer orderId;
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
