package Repair.it.Entity.Review;

import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private OperatorGarageRegisterSide garage;

    @OneToOne
    @JoinColumn(name = "request_id", unique = true)
    private CustomerRequestEntity request;

    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
