package Repair.it.Dtos.AsminSideDtos;

import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import Repair.it.Entity.User;
import Repair.it.Enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestTableResponse {
    private Long id;

    private User customer;
    private OperatorGarageRegisterSide operator;

    private VechicleType vechicleType;

    private double latitude;

    private double longitude;

    private String description;

    private RegisterStatus status;

    private String message;
    private Double price;
    private Double adminCommission;
    private LocalDateTime createdAt;
    private boolean paid;
}
