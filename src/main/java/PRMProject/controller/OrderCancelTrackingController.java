package PRMProject.controller;


import PRMProject.model.OrderCancelTrackingDto;
import PRMProject.service.OrderCancelTrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/order-cancel")
public class OrderCancelTrackingController {

    @Autowired
    private OrderCancelTrackingService orderCancelTrackingService;

    @PostMapping
    public ResponseEntity<OrderCancelTrackingDto> cancelOrder(@RequestBody OrderCancelTrackingDto dto) {
        try {
            log.info("cancelOrder");
            OrderCancelTrackingDto resultDto = orderCancelTrackingService.cancelOrder(dto);
            if (resultDto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } else
                return ResponseEntity.ok(orderCancelTrackingService.cancelOrder(resultDto));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        } finally {
            log.info("cancelOrder");
        }
    }

}
