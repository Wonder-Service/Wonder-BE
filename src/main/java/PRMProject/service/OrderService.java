package PRMProject.service;

import PRMProject.entity.Order;
import PRMProject.model.FeedbackOrderDTO;
import PRMProject.model.OrderDTO;
import PRMProject.model.OrderResultDTO;
import PRMProject.model.RequestOrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderResultDTO> getAll();

    OrderResultDTO getById(Long id);

    OrderDTO requestOrder(RequestOrderDTO requestOrderDTO) throws Exception;

    Order acceptOrder(Long orderId, Long workerId) throws Exception;

    void feedbackOrder(Long id, FeedbackOrderDTO feedbackOrderDTO) throws Exception;

    void completeOrder(Long id) throws Exception;

    List<OrderResultDTO> getAllOrderByJWT();

    List<OrderResultDTO> getAllWorkerOrderByJWT();

}
