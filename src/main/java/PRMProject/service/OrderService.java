package PRMProject.service;

import PRMProject.entity.Order;
import PRMProject.model.RequestOrderDTO;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    List<Order> getAll(String workerName);

    Order getById (Long id);

    Order requestOrder(RequestOrderDTO requestOrderDTO) throws Exception;

    Order acceptOrder(long orderId) throws Exception;

}
