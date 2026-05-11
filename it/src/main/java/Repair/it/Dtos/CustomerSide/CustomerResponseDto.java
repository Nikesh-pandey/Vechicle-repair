package Repair.it.Dtos.CustomerSide;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class CustomerResponseDto {
    private String operatorName;
    private double distance;
private String image;
}
