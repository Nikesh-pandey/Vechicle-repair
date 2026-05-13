package Repair.it.Dtos.OperatorSideDtos;

import Repair.it.Entity.OperatorSide.VechicleType;
import Repair.it.Entity.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class GetcustomerRequest {

    private String name;
    private String phoneNumber;
    private double latitude;
    private double longitude;
    private VechicleType type;
private String image;
private String description;
private String status;

}
