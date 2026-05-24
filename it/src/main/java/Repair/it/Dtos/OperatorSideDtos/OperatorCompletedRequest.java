package Repair.it.Dtos.OperatorSideDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorCompletedRequest {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String vehicleType;
    private String description;
    private String message;
    private LocalDateTime createdAt;


}
