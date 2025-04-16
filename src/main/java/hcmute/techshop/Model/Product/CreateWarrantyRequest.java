package hcmute.techshop.Model.Product;

import hcmute.techshop.Enum.WarrantyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateWarrantyRequest {
    private Integer userId;
    private Integer productId;
    private WarrantyStatus status;
    private LocalDateTime endDate;
}