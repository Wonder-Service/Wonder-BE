package PRMProject.controller;


import PRMProject.entity.Order;
import PRMProject.entity.User;
import PRMProject.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAll(@RequestParam(required = false) String workerName) {
        try {
            log.info("getAll");
            List<Order> order = orderService.getAll(workerName);
            return ResponseEntity.ok(order);
        } finally {
            log.info("getAll");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        try {
            log.info("getById");
            Order order = orderService.getById(id);
            return ResponseEntity.ok(order);
        } finally {
            log.info("getById");
        }
    }
}
