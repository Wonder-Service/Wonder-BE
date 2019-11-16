package PRMProject.controller;


import PRMProject.entity.User;
import PRMProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody User user) {
        try{
            User createdUser = userService.createUser(user);
            return new ResponseEntity(createdUser, HttpStatus.OK);
        }finally {

        }
    }

}
