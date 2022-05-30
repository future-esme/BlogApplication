package com.esempla.test.demo.service;

import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.repository.UserRepository;
import com.esempla.test.demo.service.dto.PostDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post save(PostDto dto) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return userRepository.findById(dto.getUserId()).map(user -> {
            Post post = new Post();
            post.setUser(user);
            post.setCreateTime(Instant.now());
            post.setTitle(dto.getTitle());
            post.setContent(dto.getContent());
            return postRepository.save(post);
        }).get();

    }

    public Optional<Post> replace(PostDto dto, Long id) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return Optional.of(
                postRepository.findById(id))
                .map(Optional::get)
                .map(post -> {
                    post.setUser(userRepository.findById(dto.getUserId()).get());
                    post.setUpdateTime(Instant.now());
                    post.setTitle(dto.getTitle());
                    post.setContent(dto.getContent());
                    return post;
                })
                .map(postRepository::save);
    }

    public Optional<Post> update(PostDto dto, Long id) {
        return  Optional.of(
                postRepository.findById(id))
                .map(Optional::get)
                .map(post -> {
                    if (dto.getTitle() != null) {
                        post.setTitle(dto.getTitle());
                    }
                    if (dto.getContent() != null) {
                        post.setContent(dto.getContent());
                    }
                    post.setUpdateTime(Instant.now());
                    return post;
                })
                .map(postRepository::save);
    }

}
