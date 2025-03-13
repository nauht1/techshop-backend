package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Repository.Order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<OrderEntity> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderEntity placeOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity updateOrderStatus(Integer orderId, String status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void cancelOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }
}