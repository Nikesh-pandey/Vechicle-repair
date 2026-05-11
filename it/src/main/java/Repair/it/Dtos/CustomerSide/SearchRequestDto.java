package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.VechicleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class SearchRequestDto {

    @NotNull(message = "Garage ID is required")
    private Long garageId;

    @NotNull(message = "Vehicle type is required")
    private VechicleType vehicleType;

    @NotNull(message = "Latitude is required")
    private double latitude;

    @NotNull(message = "Longitude is required")
    private double longitude;

    @NotNull(message = "Description is required")
    private String description;

    private String image;

}
