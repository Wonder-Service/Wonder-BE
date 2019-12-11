package PRMProject.service.imp;

import PRMProject.config.mapper.UserMapper;
import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.entity.Order;
import PRMProject.entity.Skill;
import PRMProject.entity.Skill_;
import PRMProject.entity.User;
import PRMProject.entity.User_;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.model.UserDto;
import PRMProject.repository.OrderRepository;
import PRMProject.repository.SkillRepository;
import PRMProject.repository.UserRepository;
import PRMProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserMapper userMapper;

    public String getRoleByUserNameAndPassword(String username, String password) {
        try {
            log.info("getRoleByUserNameAndPassword");
            return userRepository.getRoleByPasswordAndUsername(password, username);
        } finally {
            log.info("getRoleByUserNameAndPassword");
        }
    }

    @Override
    public User createUser(User user) {
        try {
            log.info("createUser");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return user;
        } finally {
            log.info("createUser");
        }
    }

    @Override
    public List<UserDto> getAll(String username, String role, Long skillId, Boolean isDelete, Boolean isMyProfile) {
        List<Specification<User>> specification = new ArrayList<>();
        if (!StringUtils.isEmpty(username)) {
            specification.add((root, query, cb) -> {
                return cb.like(cb.upper(root.get(User_.username)), "%" + username.toUpperCase().trim() + "%", '\\');
            });
        }
        if (isMyProfile != null) {
            specification.add((root, query, cb) -> {
                return cb.equal(cb.upper(root.get(User_.username)), JWTVerifier.USERNAME.toUpperCase().trim());
            });
        }
        if (!StringUtils.isEmpty(role)) {
            specification.add((root, query, cb) -> {
                return cb.equal(cb.upper(root.get(User_.role)), role.toUpperCase().trim());
            });
        }
        if (!StringUtils.isEmpty(skillId)) {
            specification.add((root, query, cb) -> {
                SetJoin<User, Skill> skill = root.join(User_.skills);
                return cb.equal(skill.get(Skill_.ID), skillId);
            });
        }
        if (!ObjectUtils.isEmpty(isDelete)) {
            specification.add((root, query, cb) -> {
                return cb.equal(root.get(User_.isDelete), isDelete);
            });
        }
        return userRepository.findAll(SpecificationBuilder.build(specification))
                .stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return userMapper.toDto(user.get());
    }

    @Override
    public List<Order> getOrderByUsername(String username) {
        return orderRepository.getByWorker_Username(username);
    }

    @Override
    public User saveDeviceId(String deviceId) {
        try {
            log.info("saveDeviceId");
            User user = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);
            User rs;
            if (user != null) {
                user.setDeviceId(deviceId);
                rs = userRepository.save(user);
            }
            return user;
        } finally {
            log.info("saveDeviceId");
        }
    }

    @Override
    public void update(Long id, UserDto userDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setDelete(userDto.isDelete());
        }
    }

    @Override
    public void addSkillToUser(Long userId, Long[] skillId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Skill> skill;
        if (user.isPresent()) {
            for (int i = 0; i < skillId.length; i++) {
                skill = skillRepository.findById(skillId[i]);
                if (skill.isPresent()) {
                    user.get().getSkills().add(skill.get());
                }
            }
            userRepository.save(user.get());
        }
    }

    @Override
    public void removeSkillOfUser(Long userId, Long[] skillId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Skill> skill;
        if (user.isPresent()) {
            for (int i = 0; i < skillId.length; i++) {
                skill = skillRepository.findById(skillId[i]);
                if (skill.isPresent()) {
                    user.get().getSkills().remove(skill.get());
                }
            }
            userRepository.save(user.get());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setDelete(true);
        }
    }
}
