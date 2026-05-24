package Repair.it.Dtos.Request;

import Repair.it.Entity.OperatorSide.VechicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class CustomerConfirmDto {
        private VechicleType vechicleType;
        private double latitude;
        private double longitude;
        private String description;
        private String image;
    }

