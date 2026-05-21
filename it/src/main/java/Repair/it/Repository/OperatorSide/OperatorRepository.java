package Repair.it.Repository.OperatorSide;

import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OperatorRepository extends JpaRepository<OperatorGarageRegisterSide,Long> {
//Optional<OperatorGarageRegisterSide> findByOperator_Id(long id);
    @Query(
            nativeQuery = true,
            value = """
select ot.* from operator_table ot where ot.operator_id=:id
"""
    )

    Optional<OperatorGarageRegisterSide> findByOperator_Id(@Param("id") Long id);




}
