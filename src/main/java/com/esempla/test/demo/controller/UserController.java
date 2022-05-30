package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.User;
import com.esempla.test.demo.errors.BadRequestException;
import com.esempla.test.demo.errors.EmailAlreadyExists;
import com.esempla.test.demo.errors.UsernameAlreadyExists;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.repository.UserRepository;
import com.esempla.test.demo.service.UserService;
import com.esempla.test.demo.service.dto.UserDto;
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

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, UserService userService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/users/{id}")
    public Optional<User> getUsers(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody UserDto user) throws BadRequestException, URISyntaxException {
        log.debug("REST request to post a User : {}", user);
        if (userRepository.findOneByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExists();
        }
        if (userRepository.findOneByEmailIgnoreCase(user.getEmail().toLowerCase()).isPresent()) {
            throw new EmailAlreadyExists();
        }
        User newUser = userService.createUser(user);
        return ResponseEntity
                .created(new URI("/api/admin/users" + newUser.getUsername()))
                .body(newUser);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto dto, @PathVariable Long id) {
        log.debug("REST request to put a User : {}, {}", dto, id);
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        Optional<User> updatedUser = userService.replace(dto, id);
        return ResponseEntity.of(updatedUser);

    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> partialUpdatePost(@NotNull @RequestBody UserDto dto, @PathVariable Long id) {
        log.debug("REST request to patch a User : {}, {}", dto, id);
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        Optional<User> updatedUser = userService.update(dto, id);
        return ResponseEntity.of(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public void deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete a User : {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }

        while (postRepository.findFirstPostByUser(userRepository.findById(id).get()).isPresent()) {
            postRepository.deletePostByUser(userRepository.findById(id).get());
        }
        while (commentRepository.findFirstCommentByUser(userRepository.findById(id).get()).isPresent()) {
            commentRepository.deleteCommentByUser(userRepository.findById(id).get());
        }
        userRepository.deleteById(id);
    }
}
