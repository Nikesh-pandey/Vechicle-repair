package Repair.it.Dtos.CustomerSide;

import Repair.it.Entity.OperatorSide.VechicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class CustomerResponseDto {
    private Long id;
    private String shopName;
    private double distance;
    private VechicleType type;
private String image;
}
