package Repair.it.Dtos.StatusHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusHistoryDto {
    private Long requestId;
    private String operatorName;
    private String operatorPhone;
    private String customerName;
    private String customerPhone;
    private String action;
    private LocalDateTime timestamp;
    private Double price;
    private String vehicleType;
    private String description;
}