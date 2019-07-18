package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.recommendations;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecommendationsRepository extends CrudRepository<recommendations,Long> {
    @Query(value = "select news_id from recommendations where user_id=? and derive_time>?", nativeQuery = true)
    public List<recommendations> findViewedNews(Long user_id,String derive_time);
}
