package hcmute.techshop.Entity.Product;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Enum.WarrantyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "warranties")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String wcode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    private WarrantyStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime updatedAt;
    
    private String cancellationReason;

    private boolean isActive = true;
}