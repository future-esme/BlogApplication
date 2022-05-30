package com.esempla.test.demo.service.dto;

import com.opencsv.bean.CsvBindByPosition;

import java.time.Instant;

public class CommentCsvDto {
    @CsvBindByPosition(position = 0)
    private Long id;
    @CsvBindByPosition(position = 1)
    private String content;
    @CsvBindByPosition(position = 2)
    private Instant createTime;
    @CsvBindByPosition(position = 3)
    private Long userId;
    @CsvBindByPosition(position = 4)
    private Long postId;

    public CommentCsvDto(Long id, String content, Instant createTime, Long userId, Long postId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.userId = userId;
        this.postId = postId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

}
