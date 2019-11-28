package PRMProject.controller;


import PRMProject.entity.Order;
import PRMProject.entity.User;
import PRMProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping
    public ResponseEntity<List<User>> getAll(@RequestParam(required = false) String username,
                                             @RequestParam(required = false) String role) {
        try {
            log.info("getAll");
            List<User> user = userService.getAll(username, role);
            return ResponseEntity.ok(user);
        } finally {
            log.info("getAll");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        try {
            log.info("getById");
            User user = userService.getById(id);
            return ResponseEntity.ok(user);
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
    public ResponseEntity createAccount(String username, String password, String role) {
        try {
            log.info("createAccount");
            User user = User.builder().username(username).password(password).role(role).build();
            User createdUser = userService.createUser(user);
            return new ResponseEntity(createdUser, HttpStatus.OK);
        } finally {
            log.info("createAccount");
        }
    }

    @PostMapping("/device-id")
    public ResponseEntity saveDeviceId(String deviceId) {
        try {
            log.info("saveDeviceId");

            User user = userService.saveDeviceId(deviceId);

            return new ResponseEntity(user, HttpStatus.OK);
        } finally {
            log.info("saveDeviceId");
        }
    }

}
