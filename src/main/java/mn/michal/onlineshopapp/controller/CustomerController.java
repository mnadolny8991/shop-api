package mn.michal.onlineshopapp.controller;

import jakarta.validation.Valid;
import mn.michal.onlineshopapp.model.dto.CustomerDTO;
import mn.michal.onlineshopapp.model.dto.OrderInputDTO;
import mn.michal.onlineshopapp.model.entity.Customer;
import mn.michal.onlineshopapp.model.entity.Order;
import mn.michal.onlineshopapp.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    HttpEntity<List<CustomerDTO>> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    @GetMapping("/{id}")
    HttpEntity<CustomerDTO> getCustomer(@PathVariable("id") Long id) {
        return customerService.getCustomer(id);
    }

    @PutMapping("/{id}")
    HttpEntity<?> updateCustomer(@PathVariable("id") Long id, @RequestBody @Valid Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    HttpEntity<?> deleteCustomer(@PathVariable("id") Long id) {
        return customerService.deleteCustomer(id);
    }

    @PostMapping
    HttpEntity<?> addCustomer(@RequestBody @Valid Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PostMapping("/{id}/add-order")
    HttpEntity<?> assignOrder(@RequestBody @Valid OrderInputDTO orderDTO,
                              @PathVariable(name = "id") Long id) {
        return customerService.assignOrder(orderDTO, id);
    }

    @GetMapping("/{id}/orders")
    HttpEntity<List<Order>> getOrders(@PathVariable(name = "id") Long id) {
        return customerService.getOrders(id);
    }
}
