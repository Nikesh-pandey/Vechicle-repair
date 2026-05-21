package Repair.it.Dtos.CustomerSide;


import Repair.it.Entity.User;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.VechicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestResponseDto {
    private User customer;
    private OperatorGarageRegisterSide operator;

    private double latitude;
    private double longitude;
    private String description;
    private String image;
}