package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class recommendations {
    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getNews_id() {
        return news_id;
    }

    public String getDerive_time() {
        return derive_time;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getDerive_algorithm() {
        return derive_algorithm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setNews_id(Long news_id) {
        this.news_id = news_id;
    }

    public void setDerive_time(String derive_time) {
        this.derive_time = derive_time;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setDerive_algorithm(int derive_algorithm) {
        this.derive_algorithm = derive_algorithm;
    }

    @Id
    private Long id;
    private Long user_id;
    private Long news_id;
    private String  derive_time;
    private String feedback;
    private int derive_algorithm;
}
