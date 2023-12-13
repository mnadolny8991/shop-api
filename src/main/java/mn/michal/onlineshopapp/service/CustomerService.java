package mn.michal.onlineshopapp.service;

import mn.michal.onlineshopapp.model.dto.CustomerDTO;
import mn.michal.onlineshopapp.model.dto.OrderInputDTO;
import mn.michal.onlineshopapp.model.entity.Customer;
import mn.michal.onlineshopapp.model.entity.Order;
import mn.michal.onlineshopapp.model.entity.OrderLine;
import mn.michal.onlineshopapp.model.repository.CustomerRepository;
import mn.michal.onlineshopapp.model.repository.OrderRepository;
import mn.michal.onlineshopapp.model.repository.ProductRepository;
import mn.michal.onlineshopapp.service.mapper.CustomerDTOMapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerDTOMapper customerDTOMapperService;
    ProductRepository productRepository;
    OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapperService, ProductRepository productRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.customerDTOMapperService = customerDTOMapperService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public HttpEntity<List<CustomerDTO>> getAllCustomers(Pageable pageable) {
        return ResponseEntity.ok(
                customerRepository.findAll(pageable).stream()
                        .map(customerDTOMapperService)
                        .toList()
        );
    }

    public HttpEntity<CustomerDTO> getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customerDTOMapperService)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public HttpEntity<?> updateCustomer(Long id, Customer customer) {
        if (customer.getId() != null) {
            if (!customer.getId().equals(id)) {
                return ResponseEntity
                        .notFound()
                        .build();
            }
        }
        var updateCustomerOpt = customerRepository.findById(id);
        if (updateCustomerOpt.isPresent()) {
            Customer updateCustomer = updateCustomerOpt.get();
            updateCustomer.updateFrom(customer);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public HttpEntity<?> deleteCustomer(Long id) {
        var customerOpt = customerRepository.findById(id);
        if (customerOpt.isPresent()) {
            customerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public HttpEntity<?> addCustomer(Customer customer) {
        if (customer.getId() != null)
            if (customerRepository.existsById(customer.getId())) {
                return ResponseEntity
                        .status(HttpStatus.valueOf(409))
                        .body("Customer with such id already exists");
            }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(409))
                    .body("Customer with such email already exists");
        }
        // TODO: password validation
        customerRepository.save(customer);
        return ResponseEntity.created(URI.create("/customer/" + customer.getId())).build();
    }

    public HttpEntity<?> assignOrder(OrderInputDTO orderDTO, Long customerId) {
        var customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.valueOf(409))
                    .body("Customer with such id does not exist");
        }
        Order order = new Order();
        order.setCustomer(customerOpt.get());
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
            if (orderLineDTO.getQuantity() > productOpt.get().getQuantity()) {
                return ResponseEntity
                        .status(HttpStatus.valueOf(409))
                        .body("Unavailable quantity");
            }
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
        order.setStatus("PENDING");
        orderRepository.save(order);
        return ResponseEntity.created(URI.create("/customers/" + customerId + "/orders")).build();
    }

    public HttpEntity<List<Order>> getOrders(Long id) {
        if (customerRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderRepository.findOrdersByCustomer_Id(id));
    }
}
