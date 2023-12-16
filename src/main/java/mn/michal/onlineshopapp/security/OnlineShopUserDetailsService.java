package mn.michal.onlineshopapp.security;

import mn.michal.onlineshopapp.model.entity.User;
import mn.michal.onlineshopapp.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OnlineShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public OnlineShopUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }
        return new OnlineShopUserDetails(user.get());
    }
}
