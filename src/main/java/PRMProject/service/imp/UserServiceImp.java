package PRMProject.service.imp;

import PRMProject.config.mapper.UserMapper;
import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.constant.Constants;
import PRMProject.entity.Order;
import PRMProject.entity.Skill;
import PRMProject.entity.Skill_;
import PRMProject.entity.User;
import PRMProject.entity.User_;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.model.UserDto;
import PRMProject.model.UserRegisterDto;
import PRMProject.repository.OrderRepository;
import PRMProject.repository.SkillRepository;
import PRMProject.repository.UserRepository;
import PRMProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public UserDto createUser(UserRegisterDto userDto) {
        try {
            log.info("createUser");
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            Set<Skill> skills =skillRepository.findAllByIdIn(userDto.getSkills());
            User user = User.builder().username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .role(userDto.getRole())
                    .email(userDto.getEmail())
                    .address(userDto.getAddress())
                    .phone(userDto.getPhone())
                    .fullname(userDto.getFullname())
                    .skills(skills)
                    .build();
            userRepository.save(user);
            return userMapper.toDto(user);
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
        UserDto result = userMapper.toDto(user.get());
        List<Order> orders = new ArrayList<>();
        switch (result.getRole()) {
            case Constants.ROLE_CUSTOMER:
                orders = orderRepository.getAllByWorkDescription_CustomerId(id);
                break;

            case Constants.ROLE_WORKER:
                orders = orderRepository.getAllByWorker_Id(id);
                break;

            default:
                break;
        }
        if (!CollectionUtils.isEmpty(orders)) {
            int rateSum = 0;
            for (Order order : orders) {
                rateSum += order.getRate();
            }
            result.setRate(rateSum / orders.size());
        }
        return result;
    }

    @Override
    public List<Order> getOrderByUsername(String username) {
//        return orderRepository.getByWorker_Username(username);
        return null;
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
            if (!ObjectUtils.isEmpty(userDto.getEmail())) {
                user.get().setEmail(userDto.getEmail());
            }
            if (!ObjectUtils.isEmpty(userDto.getPhone())) {
                user.get().setPhone(userDto.getPhone());
            }
            if (!ObjectUtils.isEmpty(userDto.getAddress())) {
                user.get().setAddress(userDto.getAddress());
            }
            if (!ObjectUtils.isEmpty(userDto.getFullname())) {
                user.get().setFullname(userDto.getFullname());
            }
            userRepository.save(user.get());
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

    public void deleteUsers(List<Long> ids) {
        userRepository.deleteUsersByIdIn(ids);
    }
}
