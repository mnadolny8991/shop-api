package mn.michal.onlineshopapp.controller;

import jakarta.validation.Valid;
import mn.michal.onlineshopapp.model.dto.UserDTO;
import mn.michal.onlineshopapp.model.dto.OrderInputDTO;
import mn.michal.onlineshopapp.model.entity.User;
import mn.michal.onlineshopapp.model.entity.Order;
import mn.michal.onlineshopapp.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    HttpEntity<List<UserDTO>> getAllusers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    HttpEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    HttpEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody @Valid User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}/delete")
    HttpEntity<?> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @PostMapping
    HttpEntity<?> addUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @PostMapping("/{id}/add-order")
    HttpEntity<?> assignOrder(@RequestBody @Valid OrderInputDTO orderDTO,
                              @PathVariable(name = "id") Long id) {
        return userService.assignOrder(orderDTO, id);
    }

    @GetMapping("/{id}/orders")
    HttpEntity<List<Order>> getOrders(@PathVariable(name = "id") Long id) {
        return userService.getOrders(id);
    }
}
