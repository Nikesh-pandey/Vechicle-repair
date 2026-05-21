package Repair.it.Entity.Request;


import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import Repair.it.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_table")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class CustomerRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User customer;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private OperatorGarageRegisterSide operator;

    @Enumerated(EnumType.STRING)
    private VechicleType vechicleType;

    private double latitude;

    private double longitude;

    private String description;

    @Enumerated(EnumType.STRING)
    private RegisterStatus status;

    private String rejectionReason;
    private String image;
    private String message;
    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = RegisterStatus.PENDING;
        }

}}
