package hcmute.techshop.Model.Order;

import lombok.Data;

@Data
public class OrderItemModel {
    private Integer productId;
    private Integer quantity;
    private Double unitPrice;
}
