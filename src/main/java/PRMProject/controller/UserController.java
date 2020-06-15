package PRMProject.controller;


import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.entity.Order;
import PRMProject.entity.User;
import PRMProject.model.DeviceDTO;
import PRMProject.model.UserDto;
import PRMProject.repository.UserRepository;
import PRMProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(required = false) String username,
                                                @RequestParam(required = false) String role,
                                                @RequestParam(required = false) Long skillId,
                                                @RequestParam(required = false) Boolean isDelete,
                                                @RequestParam(required = false) Boolean isMyProfile) {

        try {
            log.info("getAll");
            List<UserDto> user = userService.getAll(username, role, skillId, isDelete, isMyProfile);
            return ResponseEntity.ok(user);
        } finally {
            log.info("getAll");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id) {
        try {
            log.info("getById");
            UserDto user = userService.getById(id);
            return ResponseEntity.ok(user);
        } finally {
            log.info("getById");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            log.info("getById");
            userService.update(id, userDto);
            return ResponseEntity.ok().build();
        } finally {
            log.info("getById");
        }
    }

    @GetMapping("/{username}/order")
    public ResponseEntity<List<Order>> getOrderByUserName(@PathVariable String username) {
        try {
            log.info("getOrderByUserName");
            List<Order> order = userService.getOrderByUsername(username);
            return ResponseEntity.ok(order);
        } finally {
            log.info("getOrderByUserName");
        }
    }

    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody UserDto userDto) {
        try {
            log.info("createAccount");
            User user = userRepository.findUserByUsernameIgnoreCase(userDto.getUsername());
            if (ObjectUtils.isNotEmpty(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            user = User.builder().username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .role(userDto.getRole())
                    .email(userDto.getEmail())
                    .address(userDto.getAddress())
                    .phone(userDto.getPhone())
                    .fullname(userDto.getFullname())
                    .build();
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } finally {
            log.info("createAccount");
        }
    }

    @PutMapping("/{userId}/skills")
    public ResponseEntity addSkillToUser(@PathVariable Long userId, @RequestParam Long[] id) {
        try {
            log.info("addSkillToUser");
            userService.addSkillToUser(userId, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            log.info("addSkillToUser");
        }
    }

    @DeleteMapping("/{userId}/skills")
    public ResponseEntity removeSkillOfUser(@PathVariable Long userId, @RequestParam Long[] id) {
        try {
            log.info("addSkillToUser");
            userService.removeSkillOfUser(userId, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } finally {
            log.info("addSkillToUser");
        }
    }

    @PostMapping("/device-id")
    public ResponseEntity saveDeviceId(@RequestBody DeviceDTO deviceDTO) {
        try {
            log.info("saveDeviceId");

            User user = userService.saveDeviceId(deviceDTO.getDeviceId());

            return new ResponseEntity(user, HttpStatus.OK);
        } finally {
            log.info("saveDeviceId");
        }
    }

    @GetMapping("/jwt")
    public ResponseEntity getUserProfileByJWT() {
        try {
            log.info("BEGIN getUserProfileByJWT");

            List<UserDto> users = userService.getAll(JWTVerifier.USERNAME, null, null, null, null);
            return new ResponseEntity(users, HttpStatus.OK);
        } finally {
            log.info("END getUserProfileByJWT");
        }
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity deleteUser(@PathVariable List<Long> ids) {
        try {
            log.info("Begin Controller Delete User");
            userService.deleteUsers(ids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().build();
        } finally {
            log.info("End Controller Delete User");
        }
    }

}
