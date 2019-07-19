package com.luochen.web.Dao.interf;

import com.luochen.web.Dao.ItemSim;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ItemSimRepository extends CrudRepository<ItemSim,Long> {

    @Query(value = "select count(*) from ItemSim ", nativeQuery = true)
    public Long findCount();
}
