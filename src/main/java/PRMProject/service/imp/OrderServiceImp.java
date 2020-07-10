package PRMProject.service.imp;

import PRMProject.config.mapper.OrderMapper;
import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.constant.Constant;
import PRMProject.constant.Constants;
import PRMProject.entity.Order;
import PRMProject.entity.Order_;
import PRMProject.entity.User;
import PRMProject.entity.User_;
import PRMProject.entity.WorkDescription;
import PRMProject.entity.WorkDescription_;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.model.Coords;
import PRMProject.model.FeedbackOrderDTO;
import PRMProject.model.OrderDTO;
import PRMProject.model.OrderResultDTO;
import PRMProject.model.RequestOrderDTO;
import PRMProject.repository.OrderRepository;
import PRMProject.repository.UserRepository;
import PRMProject.repository.WorkDescriptionRepository;
import PRMProject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;
import java.io.IOException;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkDescriptionRepository workDescriptionRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<OrderResultDTO> getAll() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public OrderResultDTO getById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.isPresent() ? orderMapper.toDto(order.get()) : null;
    }

    @Override
    public OrderDTO requestOrder(RequestOrderDTO requestOrderDTO) throws Exception {
        try {
            log.info("Begin request Order");
            //create order

            User user = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);

            Optional<User> customer = userRepository.findById(user.getId());

            WorkDescription workDescription = WorkDescription.builder()
                    .customer(customer.get())
                    .description(requestOrderDTO.getDescription()).skillId(requestOrderDTO.getSkillId()).build();
//
            workDescription = workDescriptionRepository.save(workDescription);

            if (user == null) {
                throw new Exception();
            }

            Order order = Order.builder().address(requestOrderDTO.getAddress())
                    .detailAddress(requestOrderDTO.getDetailAddress())
                    .workDescription(workDescription)
                    .nameDevice(requestOrderDTO.getNameDevice())
                    .lat(requestOrderDTO.getCoords().getLatitude())
                    .lng(requestOrderDTO.getCoords().getLongitude())
                    .status(Constant.STATUS_PROCESSING)
                    .build();

            order = orderRepository.save(order);

            //dto for send notification
            OrderDTO dto = OrderDTO.builder().orderId(order.getId()).address(order.getAddress())
                    .description(order.getWorkDescription().getDescription())
                    .customerPhone(user.getPhone())
                    .addressDetail(requestOrderDTO.getDetailAddress())
                    .coords(new Coords(requestOrderDTO.getCoords().getLatitude(), requestOrderDTO.getCoords().getLongitude()))
                    .deviceId(user.getDeviceId())
                    .notificationType(Constant.NOTIFICATION_TYPE_REQEST)
                    .customerName(user.getFullname())
                    .build();

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/");
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildren().forEach((snap -> {
                        log.info(snap.getKey());
                        try {
                            //notification to Workers
                            sendNotification(snap.getKey(), dto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return dto;
        } finally {
            log.info("End request Order");
        }
    }

    @Override
    public Order acceptOrder(Long orderId, Long workerId) throws Exception {
        try {
            Order rs = null;
            log.info("accept Order Service");

            Order order = orderRepository.getOne(orderId);
            if (order.getWorker() != null) {
                throw new Exception();
            }

            User worker = userRepository.getOne(workerId);
            order.setWorker(worker);
            order.setStatus(Constant.NOTIFICATION_TYPE_ACCEPT);
            orderRepository.save(order);

            sendNotification(worker.getDeviceId(), OrderDTO.builder()
                    .coords(new Coords(order.getLat(),order.getLng())).notificationType(Constant.NOTIFICATION_TYPE_ACCEPT).build());

            return rs;
        } finally {
            log.info("accept Order Service");
        }
    }

    @Override
    public void feedbackOrder(Long id, FeedbackOrderDTO feedbackOrderDTO) throws Exception {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isPresent()){
            order.get().setRate(feedbackOrderDTO.getRate());
            order.get().setFeedback(feedbackOrderDTO.getFeedback());
            orderRepository.save(order.get());
        }
        else throw new Exception();
    }

    @Override
    public void completeOrder(Long id) throws Exception {
        try{
            log.info("BEGIN SERVICE Complete Order");
            //get order
            Order order = orderRepository.getOne(id);

            if(order == null) {
                throw new Exception();
            }

            order.setStatus(Constant.STATUS_COMPLETED);
            //update order
            orderRepository.save(order);
            //get user device
            Long userId = order.getWorkDescription().getCustomer().getId();

            String deviceId = userRepository.getOne(userId).getDeviceId();


            //send notification

            sendNotification(deviceId, OrderDTO.builder().notificationType(Constant.NOTIFICATION_TYPE_COMPELETE).build());
        }finally {
            log.info("BEGIN SERVICE Complete Order");
        }
    }

    @Override
    public List<OrderResultDTO> getAllOrderByJWT() {
        try {
            log.info("BEGIN getAllOrderByService");
            User currentUser = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);
            List<OrderResultDTO> rs;
            rs = orderRepository.getAllByWorkDescription_CustomerId(currentUser.getId()).stream().map(orderMapper::toDto).collect(Collectors.toList());
            return rs;
        } finally {
            log.info("End getAllOrderByService");
        }
    }

    @Override
    public List<OrderResultDTO> getAllWorkerOrderByJWT() {
        try {
            log.info("BEGIN getAllWorkerOrderByJWT");
            List<OrderResultDTO> rs;
            rs = orderRepository.findAllByWorker_Username(JWTVerifier.USERNAME).stream().map(orderMapper::toDto).collect(Collectors.toList());
            return rs;
        } finally {
            log.info("End getAllWorkerOrderByJWT");
          }
    }
  
    @Override
    public List<OrderResultDTO> getAllOrderByJWTSkills() {
        try {
            log.info("BEGIN getAllOrderByJWTSkills");
            List<OrderResultDTO> rs;
            User user = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);

            rs = orderRepository.getAllByStatusAndWorkDescription_SkillIdIn(Constant.STATUS_PROCESSING, user.getSkills().stream().map(item -> item.getId()).collect(Collectors.toSet())).stream().map(orderMapper::toDto).collect(Collectors.toList());
            return rs;
        } finally {
            log.info("End getAllOrderByJWTSkills");
        }
    }

    @Override
    public OrderResultDTO updateOrder(OrderResultDTO orderDTO) {
        try {
            log.info("updateOrder Service");
            if (orderDTO.getId() != null) {
                Optional<Order> orderOptional = orderRepository.findById(orderDTO.getId());
                if (orderOptional.isPresent()) {
                    Order rs = orderOptional.get();
                    if (orderDTO.getStatus() != null) {
                        rs.setStatus(orderDTO.getStatus());
                    }
                    if (orderDTO.getTotalCredit() != null) {
                        rs.setPrice(orderDTO.getTotalCredit());
                    }
                    if (orderDTO.getFeedback() != null) {
                        rs.setFeedback(orderDTO.getFeedback());
                    }
                    if (orderDTO.getRate() != 0) {
                        rs.setRate(orderDTO.getRate());
                    }
                    rs = orderRepository.save(rs);
                    return orderMapper.toDto(rs);
                }
            }
            return null;
        }finally {
            log.info("updateOrder Service");
        }
    }

    public static void sendNotification(String deviceId, Object data) throws IOException {
        String result = "";
        String token = "ExponentPushToken[" + deviceId + "]";
        HttpPost post = new HttpPost("https://expo.io/--/api/v2/push/send");
        StringBuilder bodyStr = new StringBuilder();
        OrderDTO orderDTO = (OrderDTO) data;
        StringBuilder json = new StringBuilder();
        json.append("{\n" +
                "\"to\":\"" + token + "\"," +
                " \"title\":\"Thanh Dep Trai Notification\",\"subtitle\":\"Tao la so 1\",\"body\":\"Dep trai hoc gioi " +
                "con nha giau va da tai\",\"data\":" +
                new ObjectMapper().writeValueAsString(orderDTO) + ",\"category\":\"asd\"}");
        log.info(json.toString());

        post.setEntity(new StringEntity(json.toString()));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
        }
        log.info("RESULT" + result);

    }

}
