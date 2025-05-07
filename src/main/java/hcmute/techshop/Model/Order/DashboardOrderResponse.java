package hcmute.techshop.Model.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOrderResponse {
    private int total;             // Total number of orders
    private int totalConfirm;      // Total number of confirmed orders
    private int totalPending;      // Total number of pending orders
    private int totalProcess;      // Total number of orders in process
    private int totalDelivering;   // Total number of orders being delivered
    private int totalDelivered;    // Total number of delivered orders
    private int totalCanceled;     // Total number of canceled orders
}