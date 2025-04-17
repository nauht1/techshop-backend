package hcmute.techshop.Model.Shipping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethodModel {
    private Integer id;
    private String name;
    private Double costPerKm;
}
