package mn.michal.onlineshopapp.controller;

import mn.michal.onlineshopapp.model.entity.Order;
import mn.michal.onlineshopapp.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public HttpEntity<List<Order>> getOrders(Pageable pageable) {
        return orderService.getOrders(pageable);
    }

    @GetMapping("/{id}")
    public HttpEntity<Order> getOrder(@PathVariable(name = "id") Long id) {
        return orderService.getOrder(id);
    }
}