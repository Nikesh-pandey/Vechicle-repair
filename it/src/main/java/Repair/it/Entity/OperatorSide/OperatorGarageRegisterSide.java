package Repair.it.Entity.OperatorSide;


import Repair.it.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "garage_table")
public class OperatorGarageRegisterSide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;

    private String shopName;
    private String address;
    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private VechicleType type;

    private String phNumber;
    private String shopImageUrl;

    @Enumerated(EnumType.STRING)
    private RegisterStatus status;
private String message;

    private LocalDateTime createdAt;
    @PrePersist
    public void onCreate(){
        createdAt = LocalDateTime.now();
    }

}