package Repair.it.Repository.Request;

import Repair.it.Entity.Request.CustomerRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequestEntity,Long> {
    Optional<CustomerRequestEntity> findByOperatorId(Long id);


    @Query(
            nativeQuery = true,
            value = """

select rt.* from garage_table gt join request_table rt on gt.id = rt.garage_id where gt.operator_id =:id and rt.status = 'PENDING'
    
    """
    )
    List<CustomerRequestEntity> findAllByLoggedInUserId(@Param("id") Long id);


}
//        SELECT rt.*
//        FROM bankApp.`request-table` rt
//        WHERE rt.customer_id = :id

// SELECT rt.* FROM `request-table` rt join operator_table ot ON ot.id = rt.operator_id where ot.operator_id= :id AND rt.status='PENDING';