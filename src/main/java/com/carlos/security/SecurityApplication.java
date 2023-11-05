package com.carlos.security;

import com.carlos.security.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.carlos.security.auth.AuthenticationService;
import com.carlos.security.auth.RegisterRequest;

import static com.carlos.security.user.Role.ADMIN;
import static com.carlos.security.user.Role.MANAGER;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
	/*
		@Bean
	public CommandLineRunner commandLineRunner(
		AuthenticationService service
	){
		return args ->{
			var admin = RegisterRequest.builder()
			.firstname("Admin")
			.lastname("Admin")
			.email("admin@gmail.com")
			.password("password")
			.role(Role.ADMIN)
			.build();
			System.out.println("admin: " + admin.getRole());
			System.out.println("Admin token: "+service.register(admin).getToken());
			System.out.println();

			var manager = RegisterRequest.builder()
			.firstname("Admin")
			.lastname("Admin")
			.email("manager@gmail.com")
			.password("password")
			.role(Role.MANAGER)
			.build();
			System.out.println("manager: "+manager.getRole());
			System.out.println("Manager token: "+service.register(manager).getToken());
		};
	}
	 */


}
