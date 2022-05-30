package com.esempla.test.demo.repository;

import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional <Post> findFirstPostByUser(User user);
    void deletePostByUser(User user);
}
