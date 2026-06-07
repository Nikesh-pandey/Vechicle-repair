package Repair.it.Dtos.AsminSideDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private long totalUsers;
    private long totalCustomers;
    private long totalOperators;
    private long totalGarages;
    private long pendingGarages;
    private long approvedGarages;
    private long rejectedGarages;
    private long totalRequests;
    private long pendingRequests;
    private long activeRequests;
    private long completedRequests;
    private long cancelledRequests;
    private double totalRevenue;
    private double totalAdminCommission;
}
