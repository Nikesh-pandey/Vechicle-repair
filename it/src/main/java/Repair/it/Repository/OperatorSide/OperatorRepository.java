package Repair.it.Repository.OperatorSide;

import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<OperatorGarageRegisterSide,Long> {
Optional<OperatorGarageRegisterSide> findByOperator_Id(long id);
}
