package mn.michal.onlineshopapp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "First name field is required")
    private String firstName;
    @NotBlank(message = "Last name field is required")
    private String lastName;
    @Email
    @NotBlank(message = "Email field is required")
    private String email;
    @NotBlank(message = "Password field is required")
    private String password;
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL
    )
    private List<Order> orders;

    public Customer updateFrom(Customer customer) {
        this.setFirstName(customer.getFirstName());
        this.setLastName(customer.getLastName());
        this.setEmail(customer.getEmail());
        this.setPassword(customer.getPassword());
        return this;
    }
}
