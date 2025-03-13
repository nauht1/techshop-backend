package hcmute.techshop.Entity.Shipping;

import hcmute.techshop.Entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String street;
    private String ward;
    private String district;
    private String province;
    private boolean isDefault;


    @ManyToOne
    @JoinColumn(name = "shipping_method_id")
    private ShippingMethodEntity shippingMethod;
}