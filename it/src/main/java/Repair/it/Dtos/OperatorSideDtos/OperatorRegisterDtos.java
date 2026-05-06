package Repair.it.Dtos.OperatorSideDtos;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import Repair.it.Entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatorRegisterDtos {
    @NotNull(message = "Shop name is required")
    private String shopName;

    @NotNull(message = "Address is required")
    private String address;

    @NotNull(message = "Latitude is required")
    private double latitude;

    @NotNull(message = "Longitude is required")
    private double longitude;

    @NotNull(message = "Vehicle type is required")
    private VechicleType type;

    @NotNull(message = "Phone number is required")
    private String phNumber;

}