package com.ahmetoral.caseproject;

import com.ahmetoral.caseproject.model.Role;
import com.ahmetoral.caseproject.requests.UserRequest;
import com.ahmetoral.caseproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@SpringBootApplication
public class caseprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(caseprojectApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run (UserService userService) {
		return args-> {
			// will run after the application has initialized
			UserRequest user1 = new UserRequest();
			user1.setUsername("admin");
			user1.setPassword("admin");
			user1.setLocked(false);

			UserRequest user2 = new UserRequest();
			user2.setUsername("test");
			user2.setPassword("test");
			user2.setLocked(false);

			if (!userService.checkRoleExists("ROLE_USER")) {
				userService.saveRole(new Role(UUID.randomUUID(), "ROLE_USER"));
			}
			if (!userService.checkRoleExists("ROLE_ADMIN")) {
				userService.saveRole(new Role(UUID.randomUUID(), "ROLE_ADMIN"));
			}

			if (!userService.checkUsernameExists(user1.getUsername())) {
				userService.saveUser(user1);
			}
			if (!userService.checkUsernameExists(user2.getUsername())) {
				userService.saveUser(user2);
			}


			userService.setUserRole("admin","ROLE_ADMIN");
			userService.setUserRole("test","ROLE_USER");

		};
	}

}
