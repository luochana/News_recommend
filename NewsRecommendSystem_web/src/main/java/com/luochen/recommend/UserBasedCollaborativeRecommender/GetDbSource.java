package com.luochen.recommend.UserBasedCollaborativeRecommender;

import com.mysql.cj.jdbc.MysqlDataSource;

public class GetDbSource {
    public static MysqlDataSource getDataSource()
    {
        MysqlDataSource dataSource = new MysqlDataSource ();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("luochen1998");
        dataSource.setDatabaseName("news_db");
        return dataSource;
    }
}
