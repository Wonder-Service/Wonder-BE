package PRMProject.service.imp;

import PRMProject.config.sercurity.JWTVerifier;
import PRMProject.entity.Order;
import PRMProject.entity.Order_;
import PRMProject.entity.User;
import PRMProject.entity.User_;
import PRMProject.entity.specifications.SpecificationBuilder;
import PRMProject.model.Coords;
import PRMProject.repository.OrderRepository;
import PRMProject.repository.UserRepository;
import PRMProject.service.OrderService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Order requestOrder(Order order) throws IOException {
        try {
            log.info("Begin request Order");
            //create order
            orderRepository.save(order);

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("/");
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot.getChildren().forEach((snap -> {
                        log.info(snap.getKey());
                        try {
                            //notification to Workers
                            sendNotification(snap.getKey());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Coords coords = new Coords();


            Order rs = null;

            return rs;
        } finally {
            log.info("End request Order");
        }
    }

    @Override
    public Order acceptOrder(long orderId) throws  Exception {
        try {
            Order rs =null;
            log.info("accept Order Service");
            Order order = orderRepository.getOne(orderId);
            if(order.getWorker()!= null) {
                throw new Exception();
            }

            User worker = userRepository.findUserByUsername(JWTVerifier.USERNAME);

            order.setWorker(worker);
            orderRepository.save(order);

            return rs;
        } finally {
            log.info("accept Order Service");
        }
    }

    public void sendNotification(String deviceId) throws IOException {
        String result = "";
        HttpPost post = new HttpPost("https://expo.io/--/api/v2/push/send");
        StringBuilder json = new StringBuilder();
        json.append("{\n" +
                "\t\"to\":"+ deviceId+"\",\n" +
                " \"title\":\"Thanh Dep Trai Notification\",\"subtitle\":\"Tao la so 1\",\"body\":\"Dep trai hoc gioi " +
                "con nha giau va da tai\",\"data\":" +
                "{\"messenger\":\"Test Notification\"},\"category\":\"asd\"}");

        post.setEntity(new StringEntity(json.toString()));
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            result = EntityUtils.toString(response.getEntity());
        }
        log.info("RESULT" + result);

    }

}
