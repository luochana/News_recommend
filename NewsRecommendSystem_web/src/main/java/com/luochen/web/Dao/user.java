package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class user {

    @Id
    private Long id;
    private String pref_list;
    private String date;
    private String password;
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getPref_list() {
        return pref_list;
    }

    public String getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPref_list(String pref_list) {
        this.pref_list = pref_list;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
