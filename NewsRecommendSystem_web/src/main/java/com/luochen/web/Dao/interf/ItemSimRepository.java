package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.ItemSim;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemSimRepository extends CrudRepository<ItemSim,Long> {

    @Query(value = "select count(*) from ItemSim ", nativeQuery = true)
    public Long findCount();

    @Query(value = "select * from ItemSim where news_id=?", nativeQuery = true)
    public ItemSim findNews(Long newsId);

    @Query(value = "select * from news_db.news_keywords where  tag=? order by newsId DESC limit ?,?;", nativeQuery = true)
    public List<ItemSim> getClassifyNews(String tag, int n, int m);
}
