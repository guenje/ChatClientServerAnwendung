package com.chatclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

/**
 * Holds the general configuration for the app.
 */

@Configuration
public class GeneralConfiguration {
    @Bean
    public RestTemplate getTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Scanner getScanner(){
        return new Scanner(System.in);
    }
}
