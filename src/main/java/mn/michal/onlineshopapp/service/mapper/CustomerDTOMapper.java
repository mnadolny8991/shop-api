package mn.michal.onlineshopapp.service.mapper;

import mn.michal.onlineshopapp.model.dto.CustomerDTO;
import mn.michal.onlineshopapp.model.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDTOMapper implements Function<Customer, CustomerDTO> {
    @Override
    public CustomerDTO apply(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
    }
}
