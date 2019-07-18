package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class newsModule {
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Id
    private Long id;
    private String name;
    private String date;
}
