package com.luochen.recommend.ContentBaseRecommender;

import com.luochen.tools.RecommKits;
import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.NewsRepository;
import com.luochen.web.Dao.news;
import com.luochen.web.Dao.newsKeywords;
import org.ansj.app.keyword.Keyword;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.luochen.recommend.ContentBaseRecommender.TFIDF.getTFIDE;


@SuppressWarnings("ALL")
public class calculateKeywords {

    public static void  makeKeywords(NewsRepository newsRepository,NewsKeywordsRepository newsKeywordsRepository)
    {

        Long num=newsRepository.findCount();
        System.out.println(num);
        for(Long i=1L;i<=num;++i)
        {
            try {
                newsKeywords newsKwd = new newsKeywords();
                Optional<news> temp = newsRepository.findById(new Long(i));
                //delete
               newsRepository.deleteById((long) i);
                news tempNews = temp.get();
                List<Keyword> kwds = getTFIDE(tempNews.getTitle(), tempNews.getContent(), 10);
                String str = new String();
                for (Keyword kw : kwds)
                    str = str + kw.getName() + "," + kw.getScore() + ";";
                System.out.println(str);
                Date date = RecommKits.getCurrenttime();
                newsKwd.setGetNewsTime(date);
                newsKwd.setKeyWords(str);
                newsKwd.setTitle(tempNews.getTitle());
                newsKwd.setContent(tempNews.getContent());
                newsKwd.setTag(tempNews.getTag());
                newsKeywordsRepository.save(newsKwd);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
       }
    }
}
