package Repair.it.Dtos.OperatorSideDtos;

import Repair.it.Dtos.Request.CustomerRequestDto;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class OperatorAcceptRequest {
    private RegisterStatus status;
    private String message;
    private Double price;


}
