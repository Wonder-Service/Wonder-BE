package PRMProject.repository;

import PRMProject.entity.Order;
import PRMProject.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllByWorker_Username(String username);

    List<Order> getAllByWorker_Id(Long id);

    List<Order> getAllByWorkDescription_CustomerId(Long id);

    List<Order> getAllByCreateBy_Username(String username);

    List<Order> getAllByStatusAndWorkDescription_SkillIdIn(String status, Set<Long> skills);
}
