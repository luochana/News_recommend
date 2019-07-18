package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<account,Integer> {
    @Query(value = "select * from account where username = ?", nativeQuery = true)
    public List<account> findByUsername(String name);
    @Query(value = "select id from account order by id DESC limit 1", nativeQuery = true)
    public String findLastAccount();
}
