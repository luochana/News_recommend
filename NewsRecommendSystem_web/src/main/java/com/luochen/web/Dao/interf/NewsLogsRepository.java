package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.newsLogs;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewsLogsRepository extends CrudRepository<newsLogs,Long> {
    @Query(value = "select * from news_logs", nativeQuery = true)
    public List<newsLogs> findAllRecord();
}
