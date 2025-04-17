package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Model.Order.PlaceOrderRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    List<OrderEntity> getOrdersByUserId(Integer userId);
    OrderModel placeOrderByCOD(PlaceOrderRequest request, Authentication auth);
    OrderEntity updateOrderStatus(Integer orderId, String status);
    void cancelOrder(Integer orderId);
}