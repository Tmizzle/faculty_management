package com.asss.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ManagementApplication {


	@Bean
	public CorsFilter corsFilter() {
		return new CorsFilter();
	}
	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}

}
