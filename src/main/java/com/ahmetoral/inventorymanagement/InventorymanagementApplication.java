package com.ahmetoral.inventorymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventorymanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventorymanagementApplication.class, args);
	}

	// breaks tests
//	@Bean
//	CommandLineRunner run (UserService userService) {
//		return args-> {
//			// will run after the application has initialized
//			userService.saveUser(new User(null, "user 1", "ahmet", "1234", new ArrayList<>()));
//			userService.saveUser(new User(null, "user 2", "mehmet", "1234", new ArrayList<>()));
//
//			userService.saveRole(new Role(null, "ROLE_USER"));
//			userService.saveRole(new Role(null, "ROLE_ADMIN"));
//
//			userService.addRoleToUser("ahmet","ROLE_ADMIN");
//			userService.addRoleToUser("ahmet","ROLE_USER");
//			userService.addRoleToUser("mehmet","ROLE_USER");
//
//		};
//	}

}
