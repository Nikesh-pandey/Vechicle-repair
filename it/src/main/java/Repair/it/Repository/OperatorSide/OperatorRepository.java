package Repair.it.Repository.OperatorSide;

import Repair.it.Entity.OperatorSide.OperatorRegisterSide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<OperatorRegisterSide,Long> {
Optional<OperatorRegisterSide> findByOperator_Id(long id);
}
