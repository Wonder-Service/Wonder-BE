package PRMProject.service;

import PRMProject.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAll(String workerName);

    Order getById (Long id);
}
