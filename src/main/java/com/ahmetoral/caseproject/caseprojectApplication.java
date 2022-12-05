package com.ahmetoral.caseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class caseprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(caseprojectApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	CommandLineRunner run (UserService userService) {
//		return args-> {
//			// will run after the application has initialized
//
//
//			User user1 = new User();
//			user1.setUsername("admin");
//			user1.setPassword("admin");
//
//			User user2 = new User();
//			user2.setUsername("test");
//			user2.setPassword("test");
//
//
//			userService.saveUser(user1);
//			userService.saveUser(user2);
//
//			userService.saveRole(new com.ahmetoral.inventorymanagement.model.Role(UUID.randomUUID(), "ROLE_USER"));
//			userService.saveRole(new com.ahmetoral.inventorymanagement.model.Role(UUID.randomUUID(), "ROLE_ADMIN"));
//
//			userService.addRoleToUser("admin","ROLE_ADMIN");
//			userService.addRoleToUser("test","ROLE_USER");
//
//		};
//	}

}
