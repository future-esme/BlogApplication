package com.esempla.test.demo.service.dto;

import lombok.ToString;

import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@ToString
@XmlType(propOrder = {"id", "content", "createTime", "postId", "userId"})
public class CommentDtoXml {
    private Long id;
    private String content;
    private XMLGregorianCalendar createTime;
    private Long postId;
    private Long userId;

    public CommentDtoXml() {
    }

    public CommentDtoXml(Long id, String content, XMLGregorianCalendar createTime, Long postId, Long userId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.postId = postId;
        this.userId = userId;
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
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime (XMLGregorianCalendar createTime) {
        this.createTime = createTime;
    }
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
