package com.esempla.test.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Configuration
public class AppConfiguration {

    private final ConfigProperties properties;

    public AppConfiguration(ConfigProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HttpEntity httpEntity() {
        String authStr = properties.getUsername() + ":" + properties.getPassword();
        String base64Credits = Base64.getEncoder().encodeToString(authStr.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credits);
        return new HttpEntity(headers);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
