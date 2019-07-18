package com.luochen.web.conf;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MahoutConf {
    private MysqlDataSource getDataSource(){
        MysqlDataSource dataSource=new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("luochen1998");
        dataSource.setDatabaseName("user");
        return dataSource;
    }
    @Bean
    public DataModel getMySQLJDBCDataModel(){
        DataModel dataModel=new MySQLJDBCDataModel(getDataSource());
        return dataModel;
    }
}
