package hcmute.techshop.Model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountModel {
    private Integer id;
    private String code;
    private Integer quantity;
    private Double value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
}
