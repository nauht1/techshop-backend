package hcmute.techshop.Entity.Order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Product.DiscountEntity;
import hcmute.techshop.Entity.Shipping.ShippingMethodEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"id", "createdAt", "isActive"}, allowGetters = true)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false)
    private String status;

    @Column(name = "shipping_addr", nullable = false)
    private String shippingAddr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id", nullable = false)
    private ShippingMethodEntity shippingMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "shipping_fee")
    private Double shippingFee;

    // Tính phí ship nếu cần
    public Double calculateShippingFee() {
        if (shippingMethod == null || shippingMethod.getMethodName() == null) return 5.0;

        switch (shippingMethod.getMethodName().toLowerCase()) {
            case "express":
                return 10.0;
            case "same-day":
                return 20.0;
            default:
                return 5.0;
        }
    }

    // Setter chuẩn chỉnh
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
