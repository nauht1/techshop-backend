package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import java.util.List;

public interface IOrderService {
    List<OrderEntity> getOrdersByUserId(Integer userId);
    OrderEntity placeOrder(OrderEntity order);
    OrderEntity updateOrderStatus(Integer orderId, String status);
    void cancelOrder(Integer orderId);
}