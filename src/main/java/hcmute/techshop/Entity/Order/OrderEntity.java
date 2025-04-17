package hcmute.techshop.Entity.Order;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import hcmute.techshop.Enum.OrderStatus;
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

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String shippingAddr;

    @ManyToOne
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethodEntity shippingMethod;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean isActive = true;
    private Double shippingFee;
    private boolean isReviewed;
    public Double calculateShippingFee() {
        // Tính phí vận chuyển tạm thời dựa trên shippingMethod
        if (shippingMethod == null) {
            return 5.0; // Default fee
        }

        switch (shippingMethod.getMethodName().toLowerCase()) {
            case "express":
                return 10.0;
            case "same-day":
                return 20.0;
            default:
                return 5.0;
        }
    }
}
