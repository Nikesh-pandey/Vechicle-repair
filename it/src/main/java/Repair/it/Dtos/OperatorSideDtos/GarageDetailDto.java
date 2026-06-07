package Repair.it.Dtos.OperatorSideDtos;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageDetailDto {
    private Long id;
    private String shopName;
    private String address;
    private double latitude;
    private double longitude;
    private VechicleType type;
    private String phNumber;
    private String shopImageUrl;
    private RegisterStatus status;
    private String message;
    private LocalDateTime createdAt;
    private Double averageRating;
    private long totalCompletedRequests;
}
