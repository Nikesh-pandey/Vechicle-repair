package Repair.it.Repository.OperatorSide;

import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OperatorRepository extends JpaRepository<OperatorGarageRegisterSide, Long> {

    @Query(nativeQuery = true, value = "SELECT gt.* FROM garage_table gt WHERE gt.operator_id = :id")
    Optional<OperatorGarageRegisterSide> findByOperator_Id(@Param("id") Long id);

    @Query(nativeQuery = true, value = "SELECT gt.* FROM garage_table gt WHERE gt.type = :#{#type.name()}")
    List<OperatorGarageRegisterSide> findAll(@Param("type") VechicleType type);

    List<OperatorGarageRegisterSide> findAllByType(VechicleType type);

    long countByStatus(RegisterStatus status);
}
