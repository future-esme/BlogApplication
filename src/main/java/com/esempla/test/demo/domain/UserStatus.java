package com.esempla.test.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "user_status")
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Size(max = 30)
    @Column(name = "status", length = 30)
    private String status;
    public UserStatus() {}

    public UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatus that = (UserStatus) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "status='" + status + '\'' +
                '}';
    }
}
