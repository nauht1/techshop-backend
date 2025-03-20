package hcmute.techshop.Controller.Shipping;

import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Service.Shipping.IShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/shipping-methods")
@RequiredArgsConstructor
public class ShippingController {
    private final IShippingService shippingMethodService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllActiveShippingMethods() {
        List<ShippingMethodEntity> shippingMethods = shippingMethodService.getAllActiveShippingMethods();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Get data successfully");
        response.put("data", shippingMethods);
        return ResponseEntity.ok(response);
    }
}

@RestController
@RequestMapping("/api/v1/admin/shipping-methods")
@RequiredArgsConstructor
class AdminShippingController {
    private final IShippingService shippingMethodService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addShippingMethod(@RequestBody ShippingMethodEntity shippingMethod) {
        ShippingMethodEntity addedMethod = shippingMethodService.addShippingMethod(shippingMethod);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Shipping method added successfully");
        response.put("data", addedMethod);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateShippingMethod(@PathVariable Integer id, @RequestBody ShippingMethodEntity newMethod) {
        ShippingMethodEntity updatedMethod = shippingMethodService.updateShippingMethod(id, newMethod);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Shipping method updated successfully");
        response.put("data", updatedMethod);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> softDeleteShippingMethod(@PathVariable Integer id) {
        shippingMethodService.softDeleteShippingMethod(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Shipping method deactivated successfully");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }
}