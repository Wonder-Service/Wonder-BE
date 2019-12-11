package PRMProject.service.imp;

import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.constant.Constant;
import PRMProject.entity.Order;
import PRMProject.entity.Order_;
import PRMProject.entity.User;
import PRMProject.entity.User_;
import PRMProject.entity.WorkDescription;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.model.Coords;
import PRMProject.model.OrderDTO;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Override
    public Order requestOrder(RequestOrderDTO requestOrderDTO) throws Exception {
        try {
            log.info("Begin request Order");
            //create order

            User user = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);


            WorkDescription workDescription = WorkDescription.builder()
                    .customerId(user.getId())
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
                    .build();

            order =  orderRepository.save(order);

            //dto for send notification
            OrderDTO dto = OrderDTO.builder().orderId(order.getId()).address(order.getAddress())
                    .description(order.getWorkDescription().getDescription())
                    .customerPhone(user.getPhone())
                    .addressDetail(requestOrderDTO.getDetailAddress())
                    .coords(new Coords(requestOrderDTO.getCoords().getLatitude(), requestOrderDTO.getCoords().getLongitude()))
                    .deviceId(user.getDeviceId())
                    .notificationType(Constant.NOTIFICATION_TYPE_REQEST)
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


            Order rs = new Order();

            return rs;
        } finally {
            log.info("End request Order");
        }
    }

    @Override
    public Order acceptOrder( Long orderId) throws Exception {
        try {
            Order rs = null;
            log.info("accept Order Service");

            Order order = orderRepository.getOne(orderId);
            if (order.getWorker() != null) {
                throw new Exception();
            }

            User worker = userRepository.findUserByUsernameIgnoreCase(JWTVerifier.USERNAME);

            order.setWorker(worker);
            orderRepository.save(order);

//            sendNotification(worker.getDeviceId(), "we found a worker");

            return rs;
        } finally {
            log.info("accept Order Service");
        }
    }

    public void sendNotification(String deviceId, Object data) throws IOException {
        String result = "";
        HttpPost post = new HttpPost("https://expo.io/--/api/v2/push/send");
        StringBuilder bodyStr = new StringBuilder();
        OrderDTO orderDTO = (OrderDTO) data;
        String token = "ExponentPushToken[" + deviceId + "]";

        StringBuilder json = new StringBuilder();
        json.append("{\n" +
                "\"to\":\""+ token+"\"," +
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
