package com.esempla.test.demo.service;

import com.esempla.test.demo.config.AppProperties;
import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.service.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;

@Service
public class ClientCommentService {
    private final Logger log = LoggerFactory.getLogger(ClientCommentService.class);
    private String CLIENT_GET_COMMENTS_URL;
    private String CLIENT_GET_COMMENT_URL;

    private final AppProperties properties;
    private final HttpEntity httpEntity;
    private final RestTemplate restTemplate;

    public ClientCommentService(AppProperties properties, HttpEntity httpEntity, RestTemplate restTemplate) {
        this.properties = properties;
        this.httpEntity = httpEntity;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void construct() {
        CLIENT_GET_COMMENTS_URL = properties.getClientUrl() + "/api/comments";
        CLIENT_GET_COMMENT_URL = properties.getClientUrl() + "/api/comments/";
    }

    public Comment[] getComments() {
        ResponseEntity<Comment[]> response = restTemplate.exchange(CLIENT_GET_COMMENTS_URL, HttpMethod.GET, httpEntity, Comment[].class);
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return response.getBody();
        }else{
            log.error("Response error with status code " + statusCode);
            throw new ResponseStatusException(statusCode);
        }
    }

    public Comment getComment(Long id) {
        CLIENT_GET_COMMENT_URL += id;
        ResponseEntity<Comment> response;
        try {
            response = restTemplate.exchange(CLIENT_GET_COMMENT_URL, HttpMethod.GET, httpEntity, Comment.class);
        } catch (HttpStatusCodeException e) {
            log.error("Response error with status code " + e);
            throw new ResponseStatusException(e.getStatusCode());
        }
        return response.getBody();
    }

    public Comment createComment(CommentDto dto) {
        HttpEntity<CommentDto> request = new HttpEntity<>(dto);
        Comment response = restTemplate.postForObject(CLIENT_GET_COMMENTS_URL, request, Comment.class);
        return response;
    }
}
