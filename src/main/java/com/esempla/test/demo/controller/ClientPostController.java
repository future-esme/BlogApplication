package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.errors.BadRequestException;
import com.esempla.test.demo.service.ClientPostService;
import com.esempla.test.demo.service.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientPostController {
        private final Logger log = LoggerFactory.getLogger(ClientPostController.class);

        private final ClientPostService clientPostService;

        public ClientPostController(ClientPostService clientPostService) {
            this.clientPostService = clientPostService;
        }

        @GetMapping("/posts")
        public Post[] getAllPosts() {
            log.debug("Client request GET all posts");
            return clientPostService.getPosts();
        }

        @GetMapping("/posts/{id}")
        public Optional<Post> getPost(@PathVariable Long id) {
            log.debug("Client request to GET post by id : {}", id);
            return Optional.ofNullable(clientPostService.getPost(id));
        }

        @PostMapping("/posts")
        public ResponseEntity<Post> addPost(@RequestBody PostDto dto) throws BadRequestException, URISyntaxException {
            log.debug("Client request to POST a post : {}", dto);
            Post newPost = clientPostService.createPost(dto);
            return ResponseEntity
                    .created(new URI("/api/admin/posts" + newPost.getTitle()))
                    .body(newPost);
        }
}
