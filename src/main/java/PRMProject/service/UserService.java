package PRMProject.service;

import PRMProject.entity.User;

public interface UserService {

    public String getRoleByUserNameAndPassword(String username, String password);

    public User createUser(User user);

}
