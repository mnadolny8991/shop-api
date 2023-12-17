package mn.michal.onlineshopapp;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OnlineShopAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopAppApplication.class, args);
		String adminPass = "admin";
		var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		System.out.println(encoder.encode(adminPass));
	}

}
