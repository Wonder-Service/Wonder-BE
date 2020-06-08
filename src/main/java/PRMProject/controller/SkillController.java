package PRMProject.controller;


import PRMProject.entity.Skill;
import PRMProject.repository.UserRepository;
import PRMProject.service.SkillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    SkillService skillService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Skill>> getAll(@RequestParam(required = false) String name) {
        try {
            log.info("getAll");
            List<Skill> skill = skillService.getAll(name);
            return ResponseEntity.ok(skill);
        } finally {
            log.info("getAll");
        }
    }
}
