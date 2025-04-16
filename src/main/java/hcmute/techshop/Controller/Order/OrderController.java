package hcmute.techshop.Controller.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Service.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getOrdersByUserId(@PathVariable Integer userId) {
        List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Get data successfully");
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody OrderEntity order) {
        // Set các giá trị mặc định
        order.setCreatedAt(LocalDateTime.now());
        order.setIsActive(true);

        // Gọi service để xử lý và lưu order
        OrderEntity placedOrder = orderService.placeOrder(order);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order placed successfully");
        response.put("data", placedOrder);

        return ResponseEntity.ok(response);
    }
}


