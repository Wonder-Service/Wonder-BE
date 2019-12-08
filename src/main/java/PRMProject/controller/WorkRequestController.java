package PRMProject.controller;


import PRMProject.config.request.WorkDescriptionDto;
import PRMProject.entity.WorkDescription;
import PRMProject.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/works")
public class WorkRequestController {

    @Autowired
    private WorkService workService;

    @GetMapping
    public ResponseEntity<List<WorkDescription>> getAll() {

        try {
            log.info("getAll");
            List<WorkDescription> workDescriptions = workService.getAll();
            return ResponseEntity.ok(workDescriptions);
        } finally {
            log.info("getAll");
        }
    }
    @PostMapping
    public ResponseEntity<WorkDescription> createWork(@RequestBody WorkDescriptionDto workDescriptionDto) {
        try {
            log.info("createWork");
            return ResponseEntity.ok(workService.createWork(workDescriptionDto));
        } finally {
            log.info("createWork");
        }
    }
}
