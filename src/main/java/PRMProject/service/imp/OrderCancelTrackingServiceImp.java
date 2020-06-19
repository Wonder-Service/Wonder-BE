package PRMProject.service.imp;

import PRMProject.config.mapper.OrderCancelTrackingMapper;
import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.constant.Constant;
import PRMProject.entity.Order;
import PRMProject.entity.OrderCancelTracking;
import PRMProject.entity.User;
import PRMProject.model.NotificationCompleteDTO;
import PRMProject.model.OrderCancelTrackingDto;
import PRMProject.model.OrderDTO;
import PRMProject.repository.OrderCancelTrackingRepository;
import PRMProject.repository.OrderRepository;
import PRMProject.repository.UserRepository;
import PRMProject.service.OrderCancelTrackingService;
import PRMProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class OrderCancelTrackingServiceImp implements OrderCancelTrackingService {
    final static int TIME_COUNT_BLOCKED = 2;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderCancelTrackingMapper orderCancelTrackingMapper;

    @Autowired
    private OrderCancelTrackingRepository orderCancelTrackingRepository;

    @Autowired
    private UserService userService;

    @Override
    public OrderCancelTrackingDto cancelOrder(OrderCancelTrackingDto dto) throws IOException {
        User user = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);
        if (user != null) {
            Optional<Order> order = orderRepository.findById(dto.getOrderId());
            if (order.isPresent()) {
                OrderCancelTracking orderCancelTracking = orderCancelTrackingMapper.toEntity(dto);
                orderCancelTracking.setUserId(user.getId());
                orderCancelTracking = orderCancelTrackingRepository.save(orderCancelTracking);

                //check to block user
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                String stringDate = simpleDateFormat.format(orderCancelTracking.getDateCancelled());
                Date dateCancel = null;
                try {
                    dateCancel = simpleDateFormat.parse(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int count = orderCancelTrackingRepository.countAllByDateCancelledAndUserId(dateCancel, orderCancelTracking.getUserId());
                if (count >= TIME_COUNT_BLOCKED) {
                    userService.deleteUser(dto.getUserId());
                } else {
                    order.get().setStatus(Constant.STATUS_CANCELED);
                    orderRepository.save(order.get());
                    OrderServiceImp.sendNotification(user.getDeviceId(), OrderDTO.builder().notificationType(Constant.NOTIFICATION_TYPE_CANCELED).build());
                }
                return orderCancelTrackingMapper.toDto(orderCancelTracking);
            }
        }
        return null;
    }
}
