package com.luochen.recommend.HotRecommender;

import com.luochen.tools.RecommKits;
import com.luochen.web.Dao.interf.NewsKeywordsRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class hotRecommender {
    public List<BigInteger> recommend(int n, int m, NewsKeywordsRepository newsKeywordsRepository)
    {
        List<BigInteger> idList=new ArrayList<BigInteger>();
        System.out.println(RecommKits.getCurrentTimeStr());
        idList=newsKeywordsRepository.getHotNewsIds(RecommKits.getCurrentTimeStr(),n,m);
        return idList;
    }
}
