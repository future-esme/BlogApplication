package com.esempla.test.demo.domain;


import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "comment_id")
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_time")
    private Instant createTime;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post postId;

    public Comment() {
    }

    public Comment(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createTime = comment.getCreateTime();
        this.postId = comment.getPostId();
        this.user = comment.getUser();
    }

    public Comment(Long id, String content, Instant createTime, Post postId, User user) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.postId = postId;
        this.user = user;
    }
    public Comment(String content, Instant createTime, Post postId, User user) {
        this.content = content;
        this.createTime = createTime;
        this.postId = postId;
        this.user = user;
    }


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPostId() {
        return postId;
    }

    public void setPostId(Post postId) {
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", postId=" + postId +
                ", user=" + user +
                '}';
    }

    public String[] ToString() {
        return new String[] {
            id.toString(),
            content.toString(),
            createTime.toString(),
            postId.toString(),
            user.toString()
        };
    }
}
