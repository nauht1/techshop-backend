package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Enum.OrderStatus;
import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Model.Order.PlaceOrderRequest;
import hcmute.techshop.Model.PageResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    PageResponse<OrderModel> getOrders(OrderStatus orderStatus, int page, int size, Authentication auth);
    OrderModel placeOrderByCOD(PlaceOrderRequest request, Authentication auth);
    OrderEntity updateOrderStatus(Integer orderId, String status);
    void cancelOrder(Integer orderId);
}