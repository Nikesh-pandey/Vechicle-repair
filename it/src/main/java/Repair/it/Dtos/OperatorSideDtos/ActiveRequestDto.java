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
public class ActiveRequestDto {
    private Long id;
    private String customerName;
    private String customerPhone;
    private double customerLat;
    private double customerLng;
    private VechicleType vehicleType;
    private String description;
    private String image;
    private RegisterStatus status;
    private Double price;
    private LocalDateTime acceptedAt;
    private LocalDateTime createdAt;
}
