package PRMProject.repository;

import PRMProject.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    @Query(value = "SELECT * FROM SKILL WHERE NAME LIKE :name", nativeQuery = true)
    List<Skill> getSkillByNameLike(@Param("name") String name);

}
