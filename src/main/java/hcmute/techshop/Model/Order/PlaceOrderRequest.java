package hcmute.techshop.Model.Order;

import hcmute.techshop.Enum.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderRequest {
    private String shippingAddress;
    private Integer shippingMethodId;
    private Double shippingFee;
    private Integer discountId; // optional
    private PaymentMethod paymentMethod;
    private List<OrderItemModel> items;
}
