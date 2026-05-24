package Repair.it.Dtos.OperatorSideDtos;


import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class OperatusStatusResponse {

    private String name;
    private String phnumber;
    private String message;
    private RegisterStatus status;
    private double customerLat;
    private double customerLng;
    private double garageLat;
    private double garageLng;
    private Double price;
    private Double adminCommission;
}
