package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.User;
import com.esempla.test.demo.errors.BadRequestException;
import com.esempla.test.demo.errors.EmailAlreadyExists;
import com.esempla.test.demo.errors.UsernameAlreadyExists;
import com.esempla.test.demo.service.ClientUserService;
import com.esempla.test.demo.service.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientUserController {
    private final Logger log = LoggerFactory.getLogger(ClientUserController.class);

    private final ClientUserService clientUserService;

    public ClientUserController(ClientUserService clientUserService) {
        this.clientUserService = clientUserService;
    }

    @GetMapping("/users")
    public User[] getAllUsers() {
        log.debug("Client request GET all users");
        return clientUserService.getUsers();
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable Long id) {
        log.debug("Client request to GET user by id : {}", id);
        return Optional.ofNullable(clientUserService.getUser(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody UserDto user) throws BadRequestException, URISyntaxException {
        log.debug("Client request to POST an user : {}", user);
        User newUser = clientUserService.createUser(user);
        return ResponseEntity
                .created(new URI("/api/admin/users" + newUser.getUsername()))
                .body(newUser);
    }

}
