package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.errors.BadRequestException;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.service.CommentService;
import com.esempla.test.demo.service.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class CommentController {
    private final Logger log = LoggerFactory.getLogger(CommentController.class);
    private static final String ENTITY_NAME = "comment";

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public CommentController(CommentRepository commentRepository, CommentService commentService) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

/*
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComments(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.of(comment);
    }
*/

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto dto) throws URISyntaxException {
        log.debug("REST request to POST Comment : {}", dto);
        Comment newComment = commentService.save(dto);
        return ResponseEntity
                .created(new URI("/api/comments" + newComment.getId()))
                .body(newComment);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<Comment> updateComment(@RequestBody CommentDto dto, @PathVariable Long id) throws BadRequestException{
        log.debug("REST request to update Comment : {}, {} ", id, dto);
        if (!commentRepository.existsById(id)) {
            throw new BadRequestException("Invalid id", ENTITY_NAME, "idnull");
        }
        Optional <Comment> updatedComment = commentService.replace(dto, id);
        return ResponseEntity.of(updatedComment);
    }

    @PatchMapping("/comment/{id}")
    public ResponseEntity<Comment> partialUpdateComment(@NotNull @RequestBody CommentDto dto, @PathVariable Long id){
        log.debug("REST request to partial update Comment : {}, {}", id, dto);
        if (!commentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        Optional <Comment> updatedComment = commentService.update(dto, id);
        return ResponseEntity.of(updatedComment);
    }
    @PreAuthorize("hasAuthority(\"" + "Administrator" + "\")")
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        if (!commentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        commentRepository.deleteById(id);
    }
}
