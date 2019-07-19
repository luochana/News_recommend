package com.luochen.web.Dao;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserCF {
    public Long getUserId() {
        return userId;
    }

    public String getRecommendStr() {
        return recommendStr;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRecommendStr(String recommendStr) {
        this.recommendStr = recommendStr;
    }

    @Id
    private Long userId;
    private String recommendStr;
}
