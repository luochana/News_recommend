package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.news;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<news,Long> {
    @Query(value = "select count(*) from news ", nativeQuery = true)
    public Long findCount();
}
