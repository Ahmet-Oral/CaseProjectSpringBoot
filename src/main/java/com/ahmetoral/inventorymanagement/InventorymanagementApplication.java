package com.ahmetoral.inventorymanagement;

import com.ahmetoral.inventorymanagement.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class InventorymanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorymanagementApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	 breaks tests
	@Bean
	CommandLineRunner run (UserService userService) {
		return args-> {
			// will run after the application has initialized
			userService.saveUser(new com.ahmetoral.inventorymanagement.model.User(null, "ahmet", "1234", new ArrayList<>(),false,false));
			userService.saveUser(new com.ahmetoral.inventorymanagement.model.User(null, "mehmet", "1234", new ArrayList<>(),false,false));

			userService.saveRole(new com.ahmetoral.inventorymanagement.model.Role(null, "ROLE_USER"));
			userService.saveRole(new com.ahmetoral.inventorymanagement.model.Role(null, "ROLE_ADMIN"));

			userService.addRoleToUser("ahmet","ROLE_USER");
			userService.addRoleToUser("ahmet","ROLE_ADMIN");
			userService.addRoleToUser("mehmet","ROLE_USER");

		};
	}

}
