package hcmute.techshop.Model.Order;

import lombok.Data;

import java.util.List;

@Data
public class OrderModel {
    private String shippingAddr;
    private Double shippingFee;
    private String status;
    private Double totalPrice;
    private Long discountId;  // Sửa Integer thành Long
    private Integer shippingMethodId;
    private Integer userId;
    private Boolean isActive;
    private List<OrderItemModel> orderItems;
}
