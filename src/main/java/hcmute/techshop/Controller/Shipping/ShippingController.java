package hcmute.techshop.Controller.Shipping;

import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Service.Shipping.IShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-methods")
@RequiredArgsConstructor
public class ShippingController {
    private final IShippingService shippingMethodService;

    @GetMapping
    public List<ShippingMethodEntity> getAllActiveShippingMethods() {
        return shippingMethodService.getAllActiveShippingMethods();
    }
}

@RestController
@RequestMapping("/api/admin/shipping-methods")
@RequiredArgsConstructor
class AdminShippingController {
    private final IShippingService shippingMethodService;

    @PostMapping
    public ResponseEntity<ShippingMethodEntity> addShippingMethod(@RequestBody ShippingMethodEntity shippingMethod) {
        return ResponseEntity.ok(shippingMethodService.addShippingMethod(shippingMethod));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingMethodEntity> updateShippingMethod(@PathVariable Integer id, @RequestBody ShippingMethodEntity newMethod) {
        return ResponseEntity.ok(shippingMethodService.updateShippingMethod(id, newMethod));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteShippingMethod(@PathVariable Integer id) {
        shippingMethodService.softDeleteShippingMethod(id);
        return ResponseEntity.ok("Shipping Method deactivated successfully");
    }
}
