package PRMProject.service.imp;

import PRMProject.entity.Order;
import PRMProject.entity.Order_;
import PRMProject.entity.User;
import PRMProject.entity.User_;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.repository.OrderRepository;
import PRMProject.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAll(String workerName) {
        List<Specification<Order>> specification = new ArrayList<>();
        if (!StringUtils.isEmpty(workerName)) {
            specification.add((root, query, cb) -> {
                Join<Order, User> orders = root.join(Order_.worker);
                return cb.like(cb.upper(orders.get(User_.username)), "%" + workerName + "%", '\\');
            });
        }
        return orderRepository.findAll(SpecificationBuilder.build(specification)).stream().collect(Collectors.toList());
    }

    @Override
    public Order getById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.isPresent() ? order.get() : null;
    }
}
