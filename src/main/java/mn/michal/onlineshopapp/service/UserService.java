package mn.michal.onlineshopapp.service;

import mn.michal.onlineshopapp.model.dto.UserDTO;
import mn.michal.onlineshopapp.model.dto.OrderInputDTO;
import mn.michal.onlineshopapp.model.entity.User;
import mn.michal.onlineshopapp.model.entity.Order;
import mn.michal.onlineshopapp.model.entity.OrderLine;
import mn.michal.onlineshopapp.model.repository.UserRepository;
import mn.michal.onlineshopapp.model.repository.OrderRepository;
import mn.michal.onlineshopapp.model.repository.ProductRepository;
import mn.michal.onlineshopapp.service.mapper.UserDTOMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;
    UserDTOMapper userDTOMapper;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    PasswordEncoder encoder;

    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper, ProductRepository productRepository, OrderRepository orderRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.encoder = encoder;
    }

    public HttpEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(
                userRepository.findAll(pageable).stream()
                        .map(userDTOMapper)
                        .toList()
        );
    }

    public HttpEntity<UserDTO> getUser(Long id) {
        return userRepository.findById(id)
                .map(userDTOMapper)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public HttpEntity<?> updateUser(Long id, User user) {
        if (user.getId() != null) {
            if (!user.getId().equals(id)) {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        }
        var updateUserOpt = userRepository.findById(id);
        if (updateUserOpt.isPresent()) {
            User updateUser = updateUserOpt.get();
            updateUser.updateFrom(user);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public HttpEntity<?> deleteUser(Long id) {
        var userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public HttpEntity<?> addUser(User user) {
        if (user.getId() != null)
            if (userRepository.existsById(user.getId())) {
                return ResponseEntity
                        .status(HttpStatus.valueOf(409))
                        .body("User with such id already exists");
            }
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(409))
                    .body("User with such email already exists");
        }
        // TODO: password validation
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return ResponseEntity.created(URI.create("/user/" + user.getId())).build();
    }

    public HttpEntity<?> assignOrder(OrderInputDTO orderDTO, Long userId) {
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(409))
                    .body("User with such id does not exist");
        }
        Order order = new Order();
        order.setUser(userOpt.get());
        double total = 0.0;
        for (var orderLineDTO : orderDTO.getOrderLines()) {
            var productOpt = productRepository.findById(orderLineDTO.getProductId());
            if (productOpt.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.valueOf(409))
                        .body("Product with such id does not exist");
            }
            OrderLine orderLine = new OrderLine();
            orderLine.setOrder(order);
            orderLine.setProduct(productOpt.get());
            int productQuantity = productOpt.get().getQuantity();
            int requestQuantity = orderLineDTO.getQuantity();
            if (requestQuantity > productQuantity) {
                return ResponseEntity
                        .status(HttpStatus.valueOf(409))
                        .body("Unavailable quantity");
            }
            productOpt.get().setQuantity(productQuantity - requestQuantity);
            orderLine.setQuantity(orderLineDTO.getQuantity());
            double price = productOpt.get().getPrice();
            orderLine.setUnitPrice(price);
            double subtotal = price * orderLineDTO.getQuantity();
            orderLine.setSubtotal(subtotal);
            total += subtotal;
            order.getOrderLines().add(orderLine);
        }
        order.setAmount(total);
        // TODO: remove magical value
        order.setStatus(Order.StatusType.PENDING);
        orderRepository.save(order);
        return ResponseEntity.created(URI.create("/users/" + userId + "/orders")).build();
    }

    public HttpEntity<List<Order>> getOrders(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderRepository.findOrdersByUser_Id(id));
    }
}
