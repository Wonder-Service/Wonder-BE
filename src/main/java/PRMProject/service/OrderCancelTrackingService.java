package PRMProject.service;

import PRMProject.model.OrderCancelTrackingDto;

import java.io.IOException;

public interface OrderCancelTrackingService {
    OrderCancelTrackingDto cancelOrder(OrderCancelTrackingDto dto) throws IOException;
}
