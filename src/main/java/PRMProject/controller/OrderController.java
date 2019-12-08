package PRMProject.controller;


import PRMProject.entity.Order;
import PRMProject.model.RequestOrderDTO;
import PRMProject.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
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

    @PostMapping
    public ResponseEntity<Order> requestOrder(@RequestBody RequestOrderDTO order) {
        try {
            log.info("requestOrder");
            Order rs = orderService.requestOrder(order);
            return ResponseEntity.ok(rs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("requestOrder");
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity acceptOrder(@PathVariable Long id) {
        try {
            log.info("requestOrder");
            Order rs = orderService.acceptOrder(id);
            return ResponseEntity.ok(rs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("This Order not avaiable");
        } finally {
            log.info("requestOrder");
        }
    }

}
