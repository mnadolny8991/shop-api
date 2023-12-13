package mn.michal.onlineshopapp.model.repository;

import mn.michal.onlineshopapp.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    public boolean existsByEmail(String email);
}
