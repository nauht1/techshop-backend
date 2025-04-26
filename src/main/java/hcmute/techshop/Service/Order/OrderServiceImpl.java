package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Entity.Order.OrderItemEntity;
import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Enum.OrderStatus;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Model.Order.OrderItemModel;
import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Model.Order.PlaceOrderRequest;
import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Repository.Order.DiscountRepository;
import hcmute.techshop.Repository.Order.OrderRepository;
import hcmute.techshop.Repository.Payment.PaymentRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Shipping.ShippingRepository;
import hcmute.techshop.Service.Tracking.ITrackingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ShippingRepository shippingRepository;
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final ITrackingService trackingService;

    @Override
    public PageResponse<OrderModel> getOrders(OrderStatus orderStatus, int page, int size, Authentication auth) {
        if (auth == null) throw new RuntimeException("Unauthorized");

        UserEntity user = (UserEntity) auth.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<OrderEntity> orderPage;

        if (orderStatus == null || orderStatus.name().equalsIgnoreCase("ALL")) {
            orderPage = orderRepository.findByUserId(user.getId(), pageable);
        } else {
            orderPage = orderRepository.findByUserIdAndStatus(user.getId(), orderStatus, pageable);
        }

        List<OrderModel> orderModels = orderPage.getContent().stream()
                .map(order -> {
                    OrderModel orderModel = modelMapper.map(order, OrderModel.class);

                    List<OrderItemModel> itemModels = order.getOrderItems().stream()
                            .map(item -> {
                                ProductEntity product = item.getProduct();

                                OrderItemModel itemModel = new OrderItemModel();
                                itemModel.setId(item.getId());
                                itemModel.setProductId(product.getId());
                                itemModel.setProductName(product.getName());
                                itemModel.setProductPrice(product.getPrice());
                                itemModel.setProductSalePrice(product.getSalePrice());
                                itemModel.setQuantity(item.getQuantity());
                                itemModel.setUnitPrice(item.getUnitPrice());
                                itemModel.setReviewed(item.isReviewed());

                                return itemModel;
                            }).toList();

                    orderModel.setItems(itemModels);
                    return orderModel;
                }).toList();

        return new PageResponse<>(
                orderModels,
                orderPage.getNumber(),
                orderPage.getTotalPages()
        );
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
    public OrderModel updateOrderStatus(Integer orderId, String status) {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        order.orElseThrow(() -> new RuntimeException("Order not found"));
        order.get().setStatus(OrderStatus.valueOf(status));
        OrderEntity savedOrder = orderRepository.save(order.get());
        return modelMapper.map(savedOrder,OrderModel.class);
    }

    @Override
    public void cancelOrder(Integer orderId, Authentication auth) {
        if (auth == null) return;

        UserEntity user = (UserEntity) auth.getPrincipal();
        trackingService.track(user, EventType.CANCEL_ORDER, "cancel_order with id" + orderId.toString());
        orderRepository.deleteById(orderId);
    }
    @Override
    public PageResponse<OrderModel> getAllOrders(OrderStatus orderStatus, int page, int size, Authentication auth) {
        if (auth == null) throw new RuntimeException("Unauthorized");

        UserEntity user = (UserEntity) auth.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<OrderEntity> orderPage;

        if (orderStatus == null || orderStatus.name().equalsIgnoreCase("ALL")) {
            orderPage = orderRepository.findAll(pageable);
        } else {
            orderPage = orderRepository.findAllByStatus(pageable,orderStatus);
        }

        List<OrderModel> orderModels = orderPage.getContent().stream()
                .map(order -> {
                    OrderModel orderModel = modelMapper.map(order, OrderModel.class);

                    List<OrderItemModel> itemModels = order.getOrderItems().stream()
                            .map(item -> {
                                ProductEntity product = item.getProduct();

                                OrderItemModel itemModel = new OrderItemModel();
                                itemModel.setId(item.getId());
                                itemModel.setProductId(product.getId());
                                itemModel.setProductName(product.getName());
                                itemModel.setProductPrice(product.getPrice());
                                itemModel.setProductSalePrice(product.getSalePrice());
                                itemModel.setQuantity(item.getQuantity());
                                itemModel.setUnitPrice(item.getUnitPrice());
                                itemModel.setReviewed(item.isReviewed());

                                return itemModel;
                            }).toList();

                    orderModel.setItems(itemModels);
                    return orderModel;
                }).toList();

        return new PageResponse<>(
                orderModels,
                orderPage.getNumber(),
                orderPage.getTotalPages()
        );
    }
}