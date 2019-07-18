package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class newsLogs {
    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getNews_id() {
        return news_id;
    }

    public Date getView_time() {
        return view_time;
    }

    public int getPrefer_degree() {
        return prefer_degree;
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

    public void setView_time(Date view_time) {
        this.view_time = view_time;
    }

    public void setPrefer_degree(int prefer_degree) {
        this.prefer_degree = prefer_degree;
    }

    @Id
    private Long id;
    private Long user_id;
    private Long news_id;
    private Date view_time;
    private int prefer_degree;
}
