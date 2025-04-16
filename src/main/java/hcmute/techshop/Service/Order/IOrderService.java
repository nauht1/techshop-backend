package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Model.Order.OrderModel;

import java.util.List;

public interface IOrderService {

    // Lấy danh sách đơn hàng của một người dùng
    List<OrderEntity> getOrdersByUserId(Integer userId);

    // Tạo đơn hàng từ OrderEntity
    OrderEntity placeOrder(OrderEntity order);

    // Tạo đơn hàng từ OrderModel (DTO)
    OrderEntity placeOrder(OrderModel orderModel);

    // Cập nhật trạng thái của đơn hàng
    OrderEntity updateOrderStatus(Integer orderId, String status);

    // Hủy đơn hàng
    void cancelOrder(Integer orderId);
}
