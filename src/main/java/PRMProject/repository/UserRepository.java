package PRMProject.repository;

import PRMProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    String getRoleByPasswordAndUsername(String password, String userName);

    User findUserByUsernameIgnoreCase(String username);

    void deleteUsersByIdIn(List<Long> ids);
}
