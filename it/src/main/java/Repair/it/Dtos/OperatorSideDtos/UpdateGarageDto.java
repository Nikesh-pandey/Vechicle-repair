package Repair.it.Dtos.OperatorSideDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGarageDto {

    @NotBlank(message = "Shop name is required")
    private String shopName;

    @NotBlank(message = "Address is required")
    private String address;

    private double latitude;

    private double longitude;

    @NotBlank(message = "Phone number is required")
    private String phNumber;
}
