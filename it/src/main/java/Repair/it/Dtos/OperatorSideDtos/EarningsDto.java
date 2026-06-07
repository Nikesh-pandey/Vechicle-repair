package Repair.it.Dtos.OperatorSideDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EarningsDto {
    private Double totalTransaction;
    private Double adminCommission;
    private Double totalIncome;
}