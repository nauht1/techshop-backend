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
public class WarrantyModel {
    private Integer id;
    private String wcode;
    private Integer userId;
    private String userEmail;
    private String userName;
    private Integer productId;
    private String productName;
    private WarrantyStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime updatedAt;
    private String cancellationReason;
    private boolean isActive;
}