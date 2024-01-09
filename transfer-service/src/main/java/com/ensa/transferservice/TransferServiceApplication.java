package com.ensa.transferservice;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RequiredArgsConstructor

@SpringBootApplication
@EnableFeignClients(basePackages = "com.ensa.transferservice.feignClients")
public class TransferServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(TransferServiceApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(TransferServiceApplication.class, args);
		logger.info("testing logging : here");

	}

}
