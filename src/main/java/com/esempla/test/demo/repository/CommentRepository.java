package com.esempla.test.demo.repository;

import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findFirstCommentByUser(User user);
    List<Comment> findAllByPostId(Post post);
    void deleteCommentByUser(User user);
}
