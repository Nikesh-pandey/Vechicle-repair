package Repair.it.Repository.ReviewSide;

import Repair.it.Entity.Review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    Optional<ReviewEntity> findByRequestId(Long requestId);

    boolean existsByRequestId(Long requestId);

    List<ReviewEntity> findByGarageId(Long garageId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.garage.id = :garageId")
    Double findAverageRatingByGarageId(@Param("garageId") Long garageId);
}
