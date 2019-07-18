package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class news {

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getAbstract() {
        return Abstract;
    }

    public String getSource() {
        return source;
    }

    public String getTag() {
        return tag;
    }

    public String getChinese_tag() {
        return chinese_tag;
    }

    public String getNews_class() {
        return news_class;
    }

    public String getSource_title() {
        return source_title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public void setAbstract(String anAbstract) {
        Abstract = anAbstract;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setChinese_tag(String chinese_tag) {
        this.chinese_tag = chinese_tag;
    }

    public void setNews_class(String news_class) {
        this.news_class = news_class;
    }

    public void setSource_title(String source_title) {
        this.source_title = source_title;
    }

    @Id
    private Long id;
    private String title;
    private String content;
    private String source_url;
    private String Abstract;
    private String source;
    private String tag;
    private String chinese_tag;
    private String news_class;
    private String source_title;

}
