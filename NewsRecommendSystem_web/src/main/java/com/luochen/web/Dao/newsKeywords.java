package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class newsKeywords {
    public Long getId() {
        return id;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Date getGetNewsTime() {
        return getNewsTime;
    }

    public void setGetNewsTime(Date getNewsTime) {
        this.getNewsTime = getNewsTime;
    }
    public int getViewNewsNum() {
        return viewNewsNum;
    }

    public void setViewNewsNum(int viewNewsNum) {
        this.viewNewsNum = viewNewsNum;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String keyWords;
    private String title;
    private String content;
    private Date getNewsTime;
    private int viewNewsNum;
    private String tag;
}
