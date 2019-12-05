package PRMProject.service;

import PRMProject.entity.Order;
import PRMProject.entity.User;
import PRMProject.model.UserDto;

import java.util.List;

public interface UserService {

    String getRoleByUserNameAndPassword(String username, String password);

    User createUser(User user);

    List<User> getAll(String username, String role);

    User getById(Long id);

    List<Order> getOrderByUsername(String username);

    User saveDeviceId(String deviceId);

    User update(Long id, UserDto userDto);

    void addSkillToUser(Long userId, Long[] skillId);
}
