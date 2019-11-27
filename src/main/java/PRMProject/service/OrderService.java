package PRMProject.service;

import PRMProject.entity.Order;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    List<Order> getAll(String workerName);

    Order getById (Long id);

    Order requestOrder(Order order) throws IOException;

    Order acceptOrder(long orderId) throws Exception;

}
