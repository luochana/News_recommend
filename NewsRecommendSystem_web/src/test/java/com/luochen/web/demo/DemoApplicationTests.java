package com.luochen.web.demo;

import com.luochen.recommend.ContentBaseRecommender.ContentBaseRecommender;
import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.NewsRepository;
import com.luochen.web.Dao.newsKeywords;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Optional;

import static com.luochen.recommend.ContentBaseRecommender.calculateKeywords.makeKeywords;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest
@ServletComponentScan("com.luochen.recommend.ContentBaseRecommender.calculateKeywords")
public class DemoApplicationTests {
    @Autowired
    private NewsKeywordsRepository newsKeywordsRepository;
    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void contextLoads() {

       makeKeywords(newsRepository,newsKeywordsRepository);
        HashMap<String,Double> map = new HashMap<String, Double>();
        Long Id= new Long(1);
        Optional<newsKeywords> targetNews=newsKeywordsRepository.findById(Id);
        newsKeywords temp=targetNews.get();
        String kwStr[]=temp.getKeyWords().split(";");
        for(String str : kwStr)
        {
            String arr[]=str.split(",");
            map.put(arr[0],Double.parseDouble(arr[1]));
        }
        ContentBaseRecommender recommender=new ContentBaseRecommender();
        HashMap<Long,Double> result=recommender.recommender(map,newsKeywordsRepository);
        for(Long tempId:result.keySet())
        {
            targetNews=newsKeywordsRepository.findById(tempId);
            temp=targetNews.get();
            System.out.println(temp.getTitle());
            System.out.println(temp.getContent());
        }

      // new Thread(new MailUtils("1329127421@qq.com",CodeUtils.generateUniqueCode()+CodeUtils.generateUniqueCode())).start();
    }

}
