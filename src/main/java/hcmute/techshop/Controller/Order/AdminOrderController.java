package hcmute.techshop.Controller.Order;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Service.Order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final IOrderService orderService;  // Đảm bảo orderService được inject đúng

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Integer orderId, @RequestParam String status) {
        OrderEntity updatedOrder = orderService.updateOrderStatus(orderId, status);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order status updated successfully");
        response.put("data", updatedOrder);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Integer orderId) {
        orderService.cancelOrder(orderId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Order cancelled successfully");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }
}
