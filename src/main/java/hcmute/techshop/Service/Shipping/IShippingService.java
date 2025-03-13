package hcmute.techshop.Service.Shipping;

import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import java.util.List;

public interface IShippingService {
    List<ShippingMethodEntity> getAllActiveShippingMethods();
    ShippingMethodEntity addShippingMethod(ShippingMethodEntity shippingMethod);
    ShippingMethodEntity updateShippingMethod(Integer id, ShippingMethodEntity newMethod);
    void softDeleteShippingMethod(Integer id);
}