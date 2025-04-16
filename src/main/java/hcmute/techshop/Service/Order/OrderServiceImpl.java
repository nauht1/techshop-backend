package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Entity.Order.OrderItemEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Repository.Order.OrderRepository;
import hcmute.techshop.Repository.Product.DiscountRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Shipping.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ShippingRepository shippingMethodRepository;

    @Override
    public List<OrderEntity> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public OrderEntity placeOrder(OrderEntity order) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public OrderEntity placeOrder(OrderModel orderModel) {
        // Tạo một đối tượng OrderEntity mới
        OrderEntity order = new OrderEntity();

        // Gán UserEntity từ userRepository
        UserEntity user = userRepository.findById(orderModel.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        // Gán ShippingMethodEntity từ shippingMethodRepository
        ShippingMethodEntity shippingMethod = shippingMethodRepository.findById(orderModel.getShippingMethodId())
                .orElseThrow(() -> new RuntimeException("Shipping method not found"));
        order.setShippingMethod(shippingMethod);

        // Gán DiscountEntity từ discountRepository nếu có
        if (orderModel.getDiscountId() != null) {
            try {
                DiscountEntity discount = discountRepository.findById(orderModel.getDiscountId())
                        .orElseThrow(() -> new RuntimeException("Discount not found"));
                order.setDiscount(discount);
            } catch (Exception e) {
                // In ra lỗi chi tiết nếu có
                e.printStackTrace();
                throw new RuntimeException("Error while setting discount: " + e.getMessage());
            }
        }



        // Gán các thuộc tính khác từ OrderModel
        order.setShippingAddr(orderModel.getShippingAddr());
        order.setShippingFee(orderModel.getShippingFee());
        order.setTotalPrice(orderModel.getTotalPrice());
        order.setStatus(orderModel.getStatus());
        order.setIsActive(orderModel.getIsActive());

        // Gán các OrderItemEntities từ OrderModel
        List<OrderItemEntity> orderItems = orderModel.getOrderItems().stream().map(itemModel -> {
            ProductEntity product = productRepository.findById(itemModel.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItemEntity item = new OrderItemEntity();
            item.setOrder(order); // Liên kết ngược với OrderEntity
            item.setProduct(product);
            item.setQuantity(itemModel.getQuantity());
            item.setUnitPrice(itemModel.getUnitPrice());
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Lưu OrderEntity vào database
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity updateOrderStatus(Integer orderId, String status) {
        // Cập nhật trạng thái đơn hàng
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    @Override
    public void cancelOrder(Integer orderId) {
        // Kiểm tra và xóa đơn hàng
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Cannot cancel. Order not found with ID: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
