package com.ensa.kycservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KycServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KycServiceApplication.class, args);
	}

}
