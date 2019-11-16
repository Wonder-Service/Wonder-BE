package PRMProject.service.imp;

import PRMProject.entity.User;
import PRMProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import PRMProject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public String getRoleByUserNameAndPassword(String username, String password) {
        try{
            String role = userRepository.getRoleByPasswordAndUsername(password,username);
            return role;
        }finally {

        }
    }

    @Override
    public User createUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return user;
        }finally {

        }
    }

}
