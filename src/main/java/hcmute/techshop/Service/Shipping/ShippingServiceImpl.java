package hcmute.techshop.Service.Shipping;

import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Repository.Shipping.ShippingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements IShippingService {
    private final ShippingRepository shippingMethodRepository;

    @Override
    public List<ShippingMethodEntity> getAllActiveShippingMethods() {
        return shippingMethodRepository.findByIsActiveTrue();
    }

    @Override
    public ShippingMethodEntity addShippingMethod(ShippingMethodEntity shippingMethod) {
        return shippingMethodRepository.save(shippingMethod);
    }

    @Override
    public ShippingMethodEntity updateShippingMethod(Integer id, ShippingMethodEntity newMethod) {
        return shippingMethodRepository.findById(id)
                .map(method -> {
                    method.setName(newMethod.getName());
                    method.setCostPerKm(newMethod.getCostPerKm());
                    method.setSpeed(newMethod.getSpeed());
                    return shippingMethodRepository.save(method);
                }).orElseThrow(() -> new RuntimeException("Shipping Method not found"));
    }

    @Override
    public void softDeleteShippingMethod(Integer id) {
        shippingMethodRepository.findById(id).ifPresent(method -> {
            method.setActive(false);
            shippingMethodRepository.save(method);
        });
    }
}