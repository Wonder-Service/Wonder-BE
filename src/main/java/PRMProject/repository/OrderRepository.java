package PRMProject.repository;

import PRMProject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> getByWorker_Username(String username);

    List<Order> getAllByWorker_Id(Long id);

    List<Order> getAllByWorkDescription_CustomerId(Long id);

    List<Order> getAllByCreateBy_Username(String username);
}
