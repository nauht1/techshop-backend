package hcmute.techshop.Service.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Entity.Order.OrderItemEntity;
import hcmute.techshop.Entity.Payment.PaymentEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import hcmute.techshop.Entity.Product.ProductImageEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Enum.EventType;
import hcmute.techshop.Enum.NotificationType;
import hcmute.techshop.Enum.OrderStatus;
import hcmute.techshop.Enum.PaymentStatus;
import hcmute.techshop.Model.Order.DashboardOrderResponse;
import hcmute.techshop.Model.Order.OrderItemModel;
import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Model.Order.PlaceOrderRequest;
import hcmute.techshop.Model.PageResponse;
import hcmute.techshop.Repository.Order.DiscountRepository;
import hcmute.techshop.Repository.Order.OrderRepository;
import hcmute.techshop.Repository.Payment.PaymentRepository;
import hcmute.techshop.Repository.Product.ProductImageRepository;
import hcmute.techshop.Repository.Product.ProductRepository;
import hcmute.techshop.Repository.Shipping.ShippingRepository;
import hcmute.techshop.Service.Notification.INotificationService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final ProductImageRepository productImageRepository;
    private final INotificationService notificationService;

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

                                ProductImageEntity productImage = productImageRepository.findFirstByProductId(product.getId())
                                        .orElse(null);

                                OrderItemModel itemModel = new OrderItemModel();
                                itemModel.setId(item.getId());
                                itemModel.setProductId(product.getId());
                                itemModel.setProductName(product.getName());
                                itemModel.setProductPrice(product.getPrice());
                                itemModel.setProductSalePrice(product.getSalePrice());
                                itemModel.setQuantity(item.getQuantity());
                                itemModel.setUnitPrice(item.getUnitPrice());
                                itemModel.setReviewed(item.isReviewed());

                                if (productImage != null) {
                                    itemModel.setProductImage(productImage.getImageUrl());
                                } else {
                                    itemModel.setProductImage(null);
                                }

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

            double unitPrice = (product.getSalePrice() != null) ? product.getSalePrice() : product.getPrice();

            OrderItemEntity item = new OrderItemEntity();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(unitPrice);

            totalPrice += unitPrice * itemRequest.getQuantity();
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice + order.getShippingFee() - discountAmount);

        // Save Order
        OrderEntity savedOrder = orderRepository.save(order);

        // Send notify
        notificationService.sendNotification(user, "Cập nhật đơn hàng",
                "Đơn hàng #" + savedOrder.getId() +" đã được đặt thành công.",
                NotificationType.ORDER_UPDATE);

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

        OrderStatus newStatus = OrderStatus.valueOf(status);
        order.get().setStatus(newStatus);
        order.get().setUpdateTime(LocalDateTime.now());

        OrderEntity savedOrder = orderRepository.save(order.get());

        // Update payment
        if (newStatus == OrderStatus.DELIVERED) {
            Optional<PaymentEntity> paymentOpt = paymentRepository.findByOrder(order.get());
            paymentOpt.ifPresent(payment -> {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
            });
        }

        // Send notify
        String message = generateMessage(status, savedOrder);
        notificationService.sendNotification(savedOrder.getUser(),
                "Cập nhật đơn hàng",
                message,
                NotificationType.ORDER_UPDATE);

        return modelMapper.map(savedOrder,OrderModel.class);
    }

    private static String generateMessage(String status, OrderEntity savedOrder) {
        String message = "Đơn hàng #" + savedOrder.getId();
        switch (status) {
            case "CONFIRMED":
                message += " của bạn đã được xác nhận";
                break;
            case "DELIVERING":
                message += " đang được giao đến bạn, vui lòng chú ý";
                break;
            case "DELIVERED":
                message += " đã giao thành công";
                break;
            case "CANCELED":
                message += " đã bị hủy";
                break;
            default: throw new RuntimeException("Something went wrong");
        }
        return message;
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

                                ProductImageEntity productImage = productImageRepository.findFirstByProductId(product.getId())
                                        .orElse(null);

                                OrderItemModel itemModel = new OrderItemModel();
                                itemModel.setId(item.getId());
                                itemModel.setProductId(product.getId());
                                itemModel.setProductName(product.getName());
                                itemModel.setProductPrice(product.getPrice());
                                itemModel.setProductSalePrice(product.getSalePrice());
                                itemModel.setQuantity(item.getQuantity());
                                itemModel.setUnitPrice(item.getUnitPrice());
                                itemModel.setReviewed(item.isReviewed());
                                if (productImage != null) {
                                    itemModel.setProductImage(productImage.getImageUrl());
                                } else {
                                    itemModel.setProductImage(null);
                                }
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
    public DashboardOrderResponse getTodayOrders(){
//        if (auth == null) throw new RuntimeException("Unauthorized");


//        Page<OrderEntity> orderPage = orderRepository;
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).toLocalDate().atStartOfDay();

        List<OrderEntity> orderPage = orderRepository.findAllByUpdateTimeBetween(startOfDay, endOfDay);
        DashboardOrderResponse resp = new DashboardOrderResponse();
        resp.setTotal(orderPage.size());
        resp.setTotalConfirm(orderPage.stream().filter(obj->obj.getStatus().equals(OrderStatus.CONFIRMED)).toList().size());
        resp.setTotalCanceled(orderPage.stream().filter(obj->obj.getStatus().equals(OrderStatus.CANCELED)).toList().size());
        resp.setTotalPending(orderPage.stream().filter(obj->obj.getStatus().equals(OrderStatus.PENDING)).toList().size());
        resp.setTotalDelivered(orderPage.stream().filter(obj->obj.getStatus().equals(OrderStatus.DELIVERED)).toList().size());
        resp.setTotalDelivering(orderPage.stream().filter(obj->obj.getStatus().equals(OrderStatus.DELIVERING)).toList().size());
        resp.setTotalProcess(orderPage.stream().filter(obj->obj.getStatus().equals(OrderStatus.PROCESSING)).toList().size());

        return resp;
//        return orderModels;
//        return new PageResponse<>(
//                orderModels,
//                orderPage.getNumber(),
//                orderPage.getTotalPages()
//        );
    }

    @Override
    public List<DashboardOrderResponse> getOrderStatisticsBetweenDates(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 90) {
            throw new IllegalArgumentException("Chỉ cho phép thống kê tối đa trong 90 ngày.");
        }

        List<DashboardOrderResponse> responseList = new ArrayList<>();

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.plusDays(1).atStartOfDay();

            List<OrderEntity> orders = orderRepository.findAllByUpdateTimeBetween(startOfDay, endOfDay);

            DashboardOrderResponse resp = new DashboardOrderResponse();
            resp.setTotal(orders.size());
            resp.setTotalConfirm((int) orders.stream().filter(o -> o.getStatus().equals(OrderStatus.CONFIRMED)).count());
            resp.setTotalPending((int) orders.stream().filter(o -> o.getStatus().equals(OrderStatus.PENDING)).count());
            resp.setTotalProcess((int) orders.stream().filter(o -> o.getStatus().equals(OrderStatus.PROCESSING)).count());
            resp.setTotalDelivering((int) orders.stream().filter(o -> o.getStatus().equals(OrderStatus.DELIVERING)).count());
            resp.setTotalDelivered((int) orders.stream().filter(o -> o.getStatus().equals(OrderStatus.DELIVERED)).count());
            resp.setTotalCanceled((int) orders.stream().filter(o -> o.getStatus().equals(OrderStatus.CANCELED)).count());

            responseList.add(resp);
        }

        return responseList;
    }
}