package hcmute.techshop.Model.Product.Discount;

import hcmute.techshop.Entity.Order.OrderEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDiscountResponse {
    private Integer id;
    private String code;
    private Integer quantity;
    private Double value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive = true;
}