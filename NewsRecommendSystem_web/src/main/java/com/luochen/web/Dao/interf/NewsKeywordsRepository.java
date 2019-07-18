package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.newsKeywords;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface NewsKeywordsRepository extends CrudRepository<newsKeywords,Long> {
    @Query(value = "select count(*) from news_keywords ", nativeQuery = true)
    public Long findCount();
    @Query(value = "select title from news_keywords where id = ?", nativeQuery = true)
    public String findTitleById(Long id);
    //not sure
    @Query(value = "select * from news_keywords where id in(:ids)", nativeQuery = true)
    public List<newsKeywords> findNewsByIds(@Param("ids") String var);

//
//    //unTest
    @Query(value = "select id from news_db.news_keywords where get_news_time>? order by view_news_num DESC limit ?,?;", nativeQuery = true)
    public List<BigInteger> getHotNewsIds(String time, int n, int m);
    @Query(value = "select * from news_db.news_keywords where get_news_time>? and tag=? order by view_news_num DESC limit ?,?;", nativeQuery = true)
    public List<newsKeywords> getClassifyNews(String time,String tag, int n, int m);
}
