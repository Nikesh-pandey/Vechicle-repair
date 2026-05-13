package Repair.it.Dtos.Request;

import Repair.it.Entity.OperatorSide.VechicleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Data
public class CustomerRequestDto {

    @NotNull(message = "Vehicle type is required")
    private VechicleType vechicleType;

    @NotNull(message = "Latitude is required")
    private double latitude;

    @NotNull(message = "Longitude is required")
    private double longitude;
private int range;

    private String description;

    private String image;
}