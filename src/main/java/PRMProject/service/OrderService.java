package PRMProject.service;

import PRMProject.entity.Order;
import PRMProject.model.FeedbackOrderDTO;
import PRMProject.model.OrderResultDTO;
import PRMProject.model.RequestOrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderResultDTO> getAll();

    Order getById(Long id);

    Order requestOrder(RequestOrderDTO requestOrderDTO) throws Exception;

    Order acceptOrder(Long orderId) throws Exception;

    void feedbackOrder(Long id, FeedbackOrderDTO feedbackOrderDTO) throws Exception;

}
