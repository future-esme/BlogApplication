package com.esempla.test.demo.service;

import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.domain.User;
import com.esempla.test.demo.domain.UserStatus;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.repository.UserRepository;
import com.esempla.test.demo.service.dto.CommentDto;
import com.esempla.test.demo.service.dto.UserDto;
import liquibase.pro.packaged.C;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Comment save(CommentDto dto){
        if (!userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        if (!postRepository.existsById(dto.getPostId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return userRepository.findById(dto.getUserId()).map(user -> {
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setCreateTime(Instant.now());
            comment.setContent((dto.getContent()));
            comment.setPostId(postRepository.findById(dto.getPostId()).get());
            return commentRepository.save(comment);
        }).get();
    }

    public Optional<Comment> replace(CommentDto dto, Long id) {
        if (!userRepository.existsById(dto.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        if (!postRepository.existsById(dto.getPostId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        return Optional
                .of(commentRepository.findById(id))
                .map(Optional::get)
                .map(comment -> {
                    comment.setUser(userRepository.findById(dto.getUserId()).get());
                    comment.setCreateTime(Instant.now());
                    comment.setContent((dto.getContent()));
                    comment.setPostId(postRepository.findById(dto.getPostId()).get());
                    return comment;
                })
                .map(commentRepository::save);
    }

    public Optional<Comment> update(CommentDto dto, Long id) {
        return Optional
                .of(commentRepository.findById(id))
                .map(Optional::get)
                .map(comment -> {
                    comment.setCreateTime(Instant.now());
                    comment.setContent((dto.getContent()));
                    return comment;
                })
                .map(commentRepository::save);
    }

}
