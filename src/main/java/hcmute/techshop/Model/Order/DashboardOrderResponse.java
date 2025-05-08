package hcmute.techshop.Model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOrderResponse {
    private int total;
    private int totalConfirm;
    private int totalPending;
    private int totalProcess;
    private int totalDelivering;
    private int totalDelivered;
    private int totalCanceled;
}
