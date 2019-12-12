package PRMProject.controller;


import PRMProject.constant.Constant;
import PRMProject.entity.Order;
import PRMProject.model.FeedbackOrderDTO;
import PRMProject.model.OrderDTO;
import PRMProject.model.OrderResultDTO;
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
    public ResponseEntity<List<OrderResultDTO>> getAll() {
        try {
            log.info("getAll");
            List<OrderResultDTO> order = orderService.getAll();
            return ResponseEntity.ok(order);
        } finally {
            log.info("getAll");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResultDTO> getById(@PathVariable Long id) {
        try {
            log.info("getById");
            OrderResultDTO order = orderService.getById(id);
            return ResponseEntity.ok(order);
        } finally {
            log.info("getById");
        }
    }

    @PostMapping
    public ResponseEntity<OrderDTO> requestOrder(@RequestBody RequestOrderDTO order) {
        try {
            log.info("requestOrder");
            OrderDTO rs = orderService.requestOrder(order);
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

    @PutMapping("/{id}/feedback")
    public ResponseEntity feedbackOrder(@PathVariable Long id, @RequestBody FeedbackOrderDTO feedbackOrderDTO) {
        try {
            log.info("feedbackOrder");
            orderService.feedbackOrder(id, feedbackOrderDTO);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("This Order not avaiable");
        } finally {
            log.info("feedbackOrder");
        }
    }

    @PutMapping("/{id}/status-complete")
    public ResponseEntity completeOrder(@PathVariable Long id) {
        try {
            log.info("Begin completeOrder Controller");
            orderService.completeOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        } finally {
            log.info("End completeOrder Controller");
        }
    }

    @PutMapping("/{id}/status-canceled")
    public ResponseEntity cancelOrder(@PathVariable Long id) {
        try {
            log.info("Begin completeOrder Controller");
            orderService.cancelOrder(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        } finally {
            log.info("End completeOrder Controller");
        }
    }
}
