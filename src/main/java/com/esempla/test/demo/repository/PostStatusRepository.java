package com.esempla.test.demo.repository;


import com.esempla.test.demo.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStatusRepository extends JpaRepository<PostStatus, Long> {
}
