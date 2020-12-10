package com.chatclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { WebMvcAutoConfiguration.class})
@EnableScheduling
public class ChatclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatclientApplication.class, args);
	}

}
