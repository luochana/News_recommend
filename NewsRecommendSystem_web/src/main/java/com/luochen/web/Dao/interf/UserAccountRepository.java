package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.userAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAccountRepository extends CrudRepository<userAccount,Integer> {
    @Query(value = "select * from user_account where username = ? or email=?", nativeQuery = true)
    public List<userAccount> findByUsername(String name,String email);
    @Query(value = "select id from user_account order by id DESC limit 1", nativeQuery = true)
    public String findLastAccount();
    @Query(value = "select * from user_account where code = ?", nativeQuery = true)
    public List<userAccount> findByCode(String code);
}
