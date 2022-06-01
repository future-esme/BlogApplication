package com.esempla.test.demo.service;

import com.esempla.test.demo.config.AppProperties;
import com.esempla.test.demo.controller.CommentController;
import com.esempla.test.demo.domain.User;
import com.esempla.test.demo.service.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class ClientUserService {

    private final Logger log = LoggerFactory.getLogger(CommentController.class);
    private String CLIENT_GET_USERS_URL;
    private String CLIENT_GET_USER_URL;
    private String CLIENT_POST_USER_URL;

    private final AppProperties properties;
    private final HttpEntity httpEntity;
    private final RestTemplate restTemplate;

    public ClientUserService(AppProperties properties, HttpEntity httpEntity, RestTemplate restTemplate) {
        this.properties = properties;
        this.httpEntity = httpEntity;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void construct() {
        CLIENT_GET_USERS_URL = properties.getClientUrl() + "/api/users";
        CLIENT_GET_USER_URL = properties.getClientUrl() + "/api/users/";
        CLIENT_POST_USER_URL = properties.getClientUrl() + "/api/register";
    }

    public User[] getUsers() {
        ResponseEntity<User[]> response = restTemplate.exchange(CLIENT_GET_USERS_URL, HttpMethod.GET, httpEntity, User[].class);
        return response.getBody();
    }

    public User getUser(Long id) {
        CLIENT_GET_USER_URL += id;
        ResponseEntity<User> response = restTemplate.exchange(CLIENT_GET_USER_URL, HttpMethod.GET, httpEntity, User.class);
        return response.getBody();
    }

    public User createUser(UserDto dto) {
        HttpEntity<UserDto> request = new HttpEntity<>(dto);
        User response = restTemplate.postForObject(CLIENT_POST_USER_URL, request, User.class);
        return response;
    }
}
