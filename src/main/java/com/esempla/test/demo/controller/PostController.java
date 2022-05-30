package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.service.PostService;
import com.esempla.test.demo.service.dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {
    private final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostRepository postRepository;
    private final PostService postService;

    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/posts/{id}")
    public Optional<Post> getAllPosts(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<Post> post = postRepository.findById(id);
        return post;
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> addPost(@RequestBody PostDto dto) throws URISyntaxException {
        log.debug("REST request to post a Post : {}", dto);
        Post newPost = postService.save(dto);
        return ResponseEntity
                .created(new URI("/api/posts" + newPost.getId()))
                .body(newPost);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Post> updatePost(@RequestBody PostDto dto, @PathVariable Long id) {
        log.debug("REST request to update a Post : {}, {}", dto, id);
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        Optional <Post> updatedPost = postService.replace(dto, id);
        return ResponseEntity.of(updatedPost);
    }

    @PatchMapping("/post/{id}")
    public ResponseEntity<Post> partialUpdatePost(@NotNull @RequestBody PostDto dto, @PathVariable Long id) {
        log.debug("REST request to partial update a Post : {}, {}", dto, id);
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        Optional <Post> updatedPost = postService.update(dto, id);
        return ResponseEntity.of(updatedPost);
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id) {
        log.debug("REST request to delete a Post : {}", id);
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        postRepository.deleteById(id);
    }
}
