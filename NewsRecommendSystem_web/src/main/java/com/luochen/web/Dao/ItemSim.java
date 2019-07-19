package com.luochen.web.Dao;


import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class ItemSim {
    public Long getNewsId() {
        return newsId;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getTag() {
        return tag;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getSim() {
        return sim;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Id
    private Long newsId;
    private String source_url;
    private String tag;
    private String articleContent;
    private String articleTitle;
    private String sim;
    private String timeStamp;
}
