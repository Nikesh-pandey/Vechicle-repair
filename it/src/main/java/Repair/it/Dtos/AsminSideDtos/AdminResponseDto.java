package Repair.it.Dtos.AsminSideDtos;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminResponseDto {

    private RegisterStatus status;
private String Message;

}
