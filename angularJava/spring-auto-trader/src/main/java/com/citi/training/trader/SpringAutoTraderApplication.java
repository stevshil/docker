package com.citi.training.trader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringAutoTraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAutoTraderApplication.class, args);
	}

}
