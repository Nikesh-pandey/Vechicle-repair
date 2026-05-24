package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.VechicleType;
import jakarta.validation.constraints.NotNull;

public class RequestServiceDto {

    @NotNull(message = "Vehicle type is required")
    private VechicleType vechicleType;
    @NotNull(message = "Latitude is required")
    private double latitude;
    @NotNull(message = "Longitude is required")
    private double longitude;


}
