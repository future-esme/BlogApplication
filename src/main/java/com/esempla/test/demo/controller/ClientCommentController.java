package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.Comment;;
import com.esempla.test.demo.errors.BadRequestException;
import com.esempla.test.demo.service.ClientCommentService;
import com.esempla.test.demo.service.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;


@RestController
@RequestMapping("/client")
public class ClientCommentController {
    private final Logger log = LoggerFactory.getLogger(ClientUserController.class);

    private final ClientCommentService clientCommentService;
    private final RestTemplate restTemplate;

    public ClientCommentController(ClientCommentService clientCommentService, RestTemplate restTemplate) {
        this.clientCommentService = clientCommentService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/comments")
    public ResponseEntity<Comment[]> getAllUsers() {
        log.debug("Client request GET all comments");
        return new ResponseEntity<>(clientCommentService.getComments(), HttpStatus.OK);
    }
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        log.debug("Client request to GET comment by id : {}", id);
        return new ResponseEntity<>(clientCommentService.getComment(id), HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto comment) throws BadRequestException, URISyntaxException {
        log.debug("Client request to POST a comment : {}", comment);
        Comment newComment = clientCommentService.createComment(comment);
        return ResponseEntity
                .created(new URI("/api/admin/users" + newComment.getId()))
                .body(newComment);
    }
}
