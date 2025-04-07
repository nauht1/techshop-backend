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
public class UpdateWarrantyRequest {
    private WarrantyStatus status;
    private LocalDateTime endDate;
    private String description;
    private String cancellationReason;
}