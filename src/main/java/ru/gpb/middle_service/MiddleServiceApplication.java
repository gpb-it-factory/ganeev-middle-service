package ru.gpb.middle_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class MiddleServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiddleServiceApplication.class, args);
	}

}
