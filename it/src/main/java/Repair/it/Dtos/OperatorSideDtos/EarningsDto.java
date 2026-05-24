package Repair.it.Dtos.OperatorSideDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningsDto {
    private Double totalTransaction;
    private Double adminCommission;
    private Double totalIncome;
}