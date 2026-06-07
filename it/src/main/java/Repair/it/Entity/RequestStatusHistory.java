package Repair.it.Entity;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.Request.CustomerRequestEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_status_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User operator;
    @ManyToOne
    private User customer;
    @ManyToOne
    private CustomerRequestEntity request;
    @Enumerated(EnumType.STRING)
    private RegisterStatus action;
    private LocalDateTime timestamp;
    @PrePersist
    public void onCreate(){
        timestamp = LocalDateTime.now();
    }
}