package PRMProject.repository;

import PRMProject.entity.WorkDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<WorkDescription, Long>, JpaSpecificationExecutor<WorkDescription> {
}
