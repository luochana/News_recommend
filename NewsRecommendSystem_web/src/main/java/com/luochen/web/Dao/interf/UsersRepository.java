package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.user;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<user, Long> {
}
