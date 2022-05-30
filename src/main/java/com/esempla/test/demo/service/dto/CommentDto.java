package com.esempla.test.demo.service.dto;

public class CommentDto {

    private String content;
    private Long userId;
    private Long postId;

    public CommentDto() {
    }

    public CommentDto(String content, Long userId, Long postId) {
        this.content = content;
        this.userId = userId;
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public String toString() {
        return "CommentDto{" +
                "content='" + content + '\'' +
                ", userId=" + userId +
                ", postId=" + postId +
                '}';
    }
}
