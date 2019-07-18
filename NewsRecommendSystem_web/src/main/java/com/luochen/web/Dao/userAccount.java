package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class userAccount {
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getState() {
        return state;
    }

    public String getCode() {
        return code;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    private Integer id;
    private String username;
    private String email;
    private  String password;
    private Integer state;
    private String code;
}
