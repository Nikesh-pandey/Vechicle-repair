package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRequestDto {
    private Long id;
    private String garageName;
    private String garagePhone;
    private String garageAddress;
    private String garageImage;
    private VechicleType vehicleType;
    private String description;
    private String image;
    private RegisterStatus status;
    private String message;
    private Double price;
    private boolean paid;
    private LocalDateTime createdAt;
    private boolean reviewed;
}
