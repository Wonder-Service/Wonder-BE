package PRMProject.repository;

import PRMProject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public String getRoleByPasswordAndUsername(String password, String userName);

    public User findUserByUsername(String username);

}
