package PRMProject.repository;

import PRMProject.entity.OrderCancelTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderCancelTrackingRepository extends JpaRepository<OrderCancelTracking, Long>, JpaSpecificationExecutor<OrderCancelTracking> {
    int countAllByDateCancelledAndUserId(Date dateCancelled, Long userId);
}
