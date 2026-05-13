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
            value= """
                    SELECT rt.* FROM `request-table` rt join operator_table ot ON ot.id = rt.operator_id where ot.operator_id= :id ;
                    """
    )
    List<CustomerRequestEntity> findAllByLoggedInUserId(@Param("id") Long id);
}
