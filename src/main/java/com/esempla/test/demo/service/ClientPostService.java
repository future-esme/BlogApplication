package com.esempla.test.demo.service;

import com.esempla.test.demo.config.ConfigProperties;
import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.service.dto.CommentDto;
import com.esempla.test.demo.service.dto.PostDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class ClientPostService {
    private String CLIENT_GET_POSTS_URL;
    private String CLIENT_GET_POST_URL;

    private final ConfigProperties properties;
    private final HttpEntity httpEntity;
    private final RestTemplate restTemplate;

    public ClientPostService(ConfigProperties properties, HttpEntity httpEntity, RestTemplate restTemplate) {
        this.properties = properties;
        this.httpEntity = httpEntity;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void construct() {
        CLIENT_GET_POSTS_URL = properties.getClientUrl() + "/api/posts";
        CLIENT_GET_POST_URL = properties.getClientUrl() + "/api/posts/";
    }

    public Post[] getPosts() {
        ResponseEntity<Post[]> response = restTemplate.exchange(CLIENT_GET_POSTS_URL, HttpMethod.GET, httpEntity, Post[].class);
        return response.getBody();
    }

    public Post getPost(Long id) {
        CLIENT_GET_POST_URL += id;
        ResponseEntity<Post> response = restTemplate.exchange(CLIENT_GET_POST_URL, HttpMethod.GET, httpEntity, Post.class);
        return response.getBody();
    }

    public Post createPost(PostDto dto) {
        HttpEntity<PostDto> request = new HttpEntity<>(dto);
        Post response = restTemplate.postForObject(CLIENT_GET_POSTS_URL, request, Post.class);
        return response;
    }
}
