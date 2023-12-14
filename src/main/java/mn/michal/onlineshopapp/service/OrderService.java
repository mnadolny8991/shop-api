package mn.michal.onlineshopapp.service;

import mn.michal.onlineshopapp.model.entity.Order;
import mn.michal.onlineshopapp.model.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public HttpEntity<List<Order>> getOrders(Pageable pageable) {
        return ResponseEntity.ok(orderRepository.findAll(pageable).stream().toList());
    }

    public HttpEntity<Order> getOrder(Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
