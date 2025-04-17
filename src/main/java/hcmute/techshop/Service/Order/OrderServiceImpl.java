package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Entity.Order.OrderItemEntity;
import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Enum.OrderStatus;
import hcmute.techshop.Enum.PaymentMethod;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Model.Order.OrderItemModel;
import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Model.Order.PlaceOrderRequest;
import hcmute.techshop.Repository.Order.DiscountRepository;
import hcmute.techshop.Repository.Order.OrderRepository;
import hcmute.techshop.Repository.Payment.PaymentRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Shipping.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ShippingRepository shippingRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public List<OrderEntity> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public OrderModel placeOrderByCOD(PlaceOrderRequest request, Authentication auth) {
        if (auth == null) return null;

        UserEntity user = (UserEntity) auth.getPrincipal();

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddr(request.getShippingAddress());

        ShippingMethodEntity shippingMethod = shippingRepository.findById(request.getShippingMethodId())
                .orElseThrow(() -> new RuntimeException("Shipping method not found"));
        order.setShippingMethod(shippingMethod);

        double discountAmount = 0.0;

        // Nếu có discount
        DiscountEntity discount;
        if (request.getDiscountId() != null) {
            discount = discountRepository.findById(request.getDiscountId())
                    .orElseThrow(() -> new RuntimeException("Discount not found"));
            order.setDiscount(discount);
            discountAmount = discount.getValue();
        }

        order.setShippingFee(request.getShippingFee());

        List<OrderItemEntity> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemModel itemRequest : request.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItemEntity item = new OrderItemEntity();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());

            totalPrice += itemRequest.getUnitPrice() * itemRequest.getQuantity();
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice + order.getShippingFee() - discountAmount);

        // Save Order
        OrderEntity savedOrder = orderRepository.save(order);

        // Save Payment
        PaymentEntity payment = new PaymentEntity();
        payment.setOrder(savedOrder);
        payment.setAmount(savedOrder.getTotalPrice());
        payment.setMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return modelMapper.map(savedOrder, OrderModel.class);
    }

    @Override
    public OrderEntity updateOrderStatus(Integer orderId, String status) {
        return null;
//        return orderRepository.findById(orderId)
//                .map(order -> {
//                    order.setStatus(status);
//                    return orderRepository.save(order);
//                }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public void cancelOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }
}