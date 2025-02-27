package hcmute.techshop.Entity.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private Double totalPrice;
    private String status;
    private String shippingAddr;

    @ManyToOne
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethodEntity shippingMethod;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
}
