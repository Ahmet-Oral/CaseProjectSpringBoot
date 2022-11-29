package com.ahmetoral.inventorymanagement;

import com.ahmetoral.inventorymanagement.model.Role;
import com.ahmetoral.inventorymanagement.model.User;
import com.ahmetoral.inventorymanagement.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class InventorymanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorymanagementApplication.class, args);
	}

	@Bean
	CommandLineRunner run (UserService userService) {
		return args-> {
			// will run after the application has initialized
			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));

			userService.saveUser(new User(null, "user 1", "ahmet", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "user 2", "mehmet", "1234", new ArrayList<>()));

			userService.addRoleToUser("ahmet","ROLE_ADMIN");
			userService.addRoleToUser("ahmet","ROLE_USER");
			userService.addRoleToUser("mehmet","ROLE_USER");
		};
	}

}
