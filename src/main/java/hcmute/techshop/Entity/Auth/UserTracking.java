package hcmute.techshop.Entity.Auth;

import hcmute.techshop.Entity.Cart.CartEntity;
import hcmute.techshop.Entity.Product.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tracking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private UserEntity user;
    private String apiPath;
    private String action;
    private String httpMethod;
    private Instant timestamp;
    private String ipAddress;
    private String requestParams;
    @ManyToOne
    private ProductEntity product;
    @ManyToOne
    private CartEntity cart;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
