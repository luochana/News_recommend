package com.luochen.tools;

import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.RecommendationsRepository;
import com.luochen.web.Dao.newsKeywords;
import com.luochen.web.Dao.recommendations;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ALL")
public class RecommKits {
    /**
     * 推荐新闻的时效性天数，即从推荐当天开始到之前beforeDays天的新闻属于仍具有时效性的新闻，予以推荐。
     */
    private static int beforeDays =-1;
    public static Timestamp getInRecTimestamp(int before_Days)
    {
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.add(Calendar.DAY_OF_MONTH, before_Days); // 设置为前beforeNum天
        return new Timestamp(calendar.getTime().getTime());
    }

    public static void filterOutDateNews(Collection<Long> col, Long userId,NewsKeywordsRepository newsKeywordsRepository)
    {
        try
        {
            String newsids = getInQueryString(col.iterator());
            if (!newsids.equals("()"))
            {
                List<newsKeywords> newsList = newsKeywordsRepository.findNewsByIds(newsids);
                for(newsKeywords news:newsList)
                {
                    if (news.getGetNewsTime().before(getInRecTimestamp(beforeDays)))
                        col.remove(news.getId());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void filterReccedNews(Collection<Long> col, Long userId, RecommendationsRepository recommendationsRepository)
    {
        try
        {
            List<recommendations> recommendationsList=recommendationsRepository.findViewedNews(userId,getInRecDate());
            for (recommendations recommendation:recommendationsList)
            {
                if (col.contains(recommendation.getNews_id()))
                {
                    col.remove(recommendation.getNews_id());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static <T> String getInQueryString(Iterator<T> ite)
    {
        String inQuery = "(";
        while (ite.hasNext())
        {
            inQuery += ite.next() + ",";
        }
        if (inQuery.length() > 1)
        {
            inQuery = inQuery.substring(0, inQuery.length() - 1);
        }
        inQuery += ")";
        return inQuery;
    }

    public static String getSpecificDayFormat(int before_Days)
    {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance(); // 得到日历
        calendar.add(Calendar.DAY_OF_MONTH, before_Days); // 设置为前beforeNum天
        Date d = calendar.getTime();
        return "'" + date_format.format(d) + "'";
    }

    public static String getInRecDate()
    {
        return getSpecificDayFormat(beforeDays);
    }
    public static void removeOverNews(Set<Long> set, int N)
    {
        int i = 0;
        Iterator<Long> ite = set.iterator();
        while (ite.hasNext())
        {
            if (i >= N)
            {
                ite.remove();
                ite.next();
            }
            else
            {
                ite.next();
            }
            i++;
        }
    }
    public static void insertRecommend(Long userId, Iterator<Long> newsIte,RecommendationsRepository recommendationsRepository)
    {
        try
        {
            while (newsIte.hasNext())
            {
                recommendations recomm=new recommendations();
                recomm.setId(userId);
                recomm.setNews_id(newsIte.next());
                recommendationsRepository.save(recomm);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static Date getCurrenttime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());
        try {
            Date getNewsTime=df.parse(date);
            return getNewsTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getCurrentTimeStr()
    {
       // getInRecTimestamp(1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(getInRecTimestamp(beforeDays));
    }


}
