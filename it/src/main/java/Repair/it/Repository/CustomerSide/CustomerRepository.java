package Repair.it.Repository.CustomerSide;

import Repair.it.Dtos.CustomerSide.CustomerRequestProjection;
import Repair.it.Entity.Request.CustomerRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerRequestEntity,Long>{
    @Query(
            nativeQuery = true,
            value = """
select rt.*,ud.* from garage_table gt join request_table rt  on gt.id=rt.garage_id  join users_data ud on gt.operator_id=ud.id where rt.customer_id=:id;
"""



    )
    List<CustomerRequestProjection> findOperatorIdByCustomerId(@Param("id") Long customerId);

    @Query(
            nativeQuery = true,
            value = """
            SELECT rt.* FROM request_table rt 
            WHERE rt.customer_id = :customerId
        """
    )
    List<CustomerRequestEntity> findByCustomerId(@Param("customerId") Long customerId);
}
