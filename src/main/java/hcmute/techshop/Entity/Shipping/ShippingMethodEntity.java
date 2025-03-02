package hcmute.techshop.Entity.Shipping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipping_methods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingMethodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double costPerKm;
    private Float speed;
    private boolean isActive = true;
}
