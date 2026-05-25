package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalResponseDto {
    private Long id;
    private String name;
    private String phnumber;
    private String message;
    private RegisterStatus status;
    private Double price;
    private boolean paid;
}