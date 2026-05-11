package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.VechicleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerRequestDto {
    @NotNull(message = "Vehicle type is required")
    private VechicleType vechicleType;
    @NotNull(message = "Latitude is required")
    private double latitude;

    @NotNull(message = "Longitude is required")
    private double longitude;
private double range;
    private String description;
private String image;
}
