package hcmute.techshop.Controller.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hcmute.techshop.Model.Order.OrderModel;
import hcmute.techshop.Model.Order.PlaceOrderRequest;
import hcmute.techshop.Model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hcmute.techshop.Entity.Order.OrderEntity;
import hcmute.techshop.Service.Order.IOrderService;
import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<ResponseModel> placeOrder(@RequestBody PlaceOrderRequest request, Authentication auth) {
        try {
            OrderModel placedOrder = orderService.placeOrderByCOD(request, auth);

            if (placedOrder == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseModel(false, "Đặt hàng thất bại", null));
            }


            return ResponseEntity.ok(new ResponseModel(true, "Đặt hàng thành công", placedOrder));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel(false, "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }
}

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
class AdminOrderController {
    private final IOrderService orderService;

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
