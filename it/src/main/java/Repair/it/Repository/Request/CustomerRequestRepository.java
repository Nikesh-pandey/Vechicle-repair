package Repair.it.Repository.Request;

import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.Request.CustomerRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequestEntity, Long> {

    Optional<CustomerRequestEntity> findByOperatorId(Long id);

    @Query(nativeQuery = true, value = """
            SELECT rt.* FROM garage_table gt
            JOIN request_table rt ON gt.id = rt.garage_id
            WHERE gt.operator_id = :id AND rt.status = 'PENDING'
            """)
    List<CustomerRequestEntity> findAllByLoggedInUserId(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT rt.* FROM garage_table gt
            JOIN request_table rt ON gt.id = rt.garage_id
            WHERE gt.operator_id = :id AND rt.status = 'COMPLETED'
            """)
    List<CustomerRequestEntity> findBystaus(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT rt.* FROM garage_table gt
            JOIN request_table rt ON gt.id = rt.garage_id
            WHERE gt.operator_id = :id
            AND rt.status NOT IN ('PENDING', 'COMPLETED', 'CANCELLED')
            """)
    List<CustomerRequestEntity> findActiveByOperatorId(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT COALESCE(SUM(rt.price), 0)
            FROM request_table rt
            JOIN garage_table gt ON rt.garage_id = gt.id
            WHERE gt.operator_id = :id AND rt.status = 'COMPLETED' AND rt.paid = true
            """)
    Double findTotalPrice(@Param("id") Long id);

    @Query(nativeQuery = true, value = """
            SELECT COALESCE(SUM(rt.price), 0)
            FROM request_table rt
            WHERE rt.status = 'COMPLETED' AND rt.paid = true
            """)
    Double findTotalRevenue();

    @Query(nativeQuery = true, value = """
            SELECT COALESCE(SUM(rt.admin_commission), 0)
            FROM request_table rt
            WHERE rt.status = 'COMPLETED' AND rt.paid = true
            """)
    Double findTotalAdminCommission();

    @Query(nativeQuery = true, value = """
            SELECT COUNT(*) FROM request_table rt
            WHERE rt.status NOT IN ('PENDING', 'COMPLETED', 'CANCELLED')
            """)
    long countActiveRequests();

    long countByStatus(RegisterStatus status);

        @Query(nativeQuery = true, value = """
                SELECT rt.* FROM request_table rt
                WHERE (:status IS NULL OR rt.status = :status)
                  AND (:vehicleType IS NULL OR rt.vechicle_type = :vehicleType)
                  AND (:customerId IS NULL OR rt.customer_id = :customerId)
                  AND (:garageId IS NULL OR rt.garage_id = :garageId)
                  AND (:adminCommission IS NULL OR rt.admin_commission = :adminCommission)
                  AND (:price IS NULL OR rt.price = :price)
                  AND (:paid IS NULL OR rt.paid = :paid)
                """,
                countQuery = """
                SELECT COUNT(*) FROM request_table rt
                WHERE (:status IS NULL OR rt.status = :status)
                  AND (:vehicleType IS NULL OR rt.vechicle_type = :vehicleType)
                  AND (:customerId IS NULL OR rt.customer_id = :customerId)
                  AND (:garageId IS NULL OR rt.garage_id = :garageId)
                  AND (:adminCommission IS NULL OR rt.admin_commission = :adminCommission)
                  AND (:price IS NULL OR rt.price = :price)
                  AND (:paid IS NULL OR rt.paid = :paid)
                """)
        Page<CustomerRequestEntity> findByFilters(
                @Param("status") String status,
                @Param("vehicleType") String vehicleType,
                @Param("customerId") Long customerId,
                @Param("garageId") Long garageId,
                @Param("adminCommission") Integer adminCommission,
                @Param("price") Integer price,
                @Param("paid") Integer paid,
                Pageable pageable
        );
}
