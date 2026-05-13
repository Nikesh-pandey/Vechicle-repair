package Repair.it.Dtos.OperatorSideDtos;


import Repair.it.Entity.OperatorSide.RegisterStatus;
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
    private String Phnumber;
    private String message;
    private RegisterStatus status;

}
