package com.example.gahona_jordan_leccion1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GahonaJordanLeccion1Application {

	public static void main(String[] args) {
		SpringApplication.run(GahonaJordanLeccion1Application.class, args);
	}

}
