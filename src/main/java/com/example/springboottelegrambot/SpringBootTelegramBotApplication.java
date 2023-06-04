package com.example.springboottelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootTelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTelegramBotApplication.class, args);
	}
}
