package hcmute.techshop.Model.Order;

import hcmute.techshop.Model.Shipping.ShippingMethodModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private Integer id;
    private String status;
    private Double totalPrice;
    private Double shippingFee;
    private String shippingAddr;
    private ShippingMethodModel shippingMethod;
    private String discountCode;
    private LocalDateTime createdAt;
    private List<OrderItemModel> items;
}
