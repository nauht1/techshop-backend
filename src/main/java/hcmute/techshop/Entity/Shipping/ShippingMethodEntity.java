package hcmute.techshop.Entity.Shipping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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


    @OneToMany(mappedBy = "shippingMethod", cascade = CascadeType.ALL)
    private List<AddressEntity> addresses;

    public String getMethodName() {
        return null;
    }
}