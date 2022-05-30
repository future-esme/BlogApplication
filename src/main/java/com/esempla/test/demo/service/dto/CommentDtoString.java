package com.esempla.test.demo.service.dto;

public class CommentDtoString {

    private String id;
    private String content;
    private String createTime;
    private String userId;
    private String postId;

    public CommentDtoString(String id, String content, String createTime, String userId, String postId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.userId = userId;
        this.postId = postId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
