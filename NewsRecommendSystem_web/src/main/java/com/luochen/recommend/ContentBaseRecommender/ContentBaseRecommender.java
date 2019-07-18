package com.luochen.recommend.ContentBaseRecommender;

import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.newsKeywords;

import java.util.*;

@SuppressWarnings("ALL")
public class ContentBaseRecommender {
    HashMap<Long,Double> tempMatchMap = new HashMap<Long, Double>();
    HashMap<String, Double> map=new HashMap<String, Double>();
    //@Autowired
  //  private NewsKeywordsRepository newsKeywordsRepository;
  //  private NewsRepository newsRepository;
    public  HashMap<Long,Double>  recommender(HashMap<String,Double> kwMap,NewsKeywordsRepository newsKeywordsRepository)
    {
        Long num=newsKeywordsRepository.findCount();
        int subNum=num.intValue()-300;
        Random rd = new Random();
        int  index = Math.abs(rd.nextInt() % subNum);
        for(int i = index+1; i<=index+300; ++i)
        {
            Optional<newsKeywords> temp=newsKeywordsRepository.findById((long) i);
            newsKeywords tempNews=temp.get();
            String kwStr[]=tempNews.getKeyWords().split(";");
            for(String str : kwStr)
            {
                try
                {
                    String arr[] = str.split(",");
                    map.put(arr[0], Double.parseDouble(arr[1]));
                }
                catch (Exception e)
                {
                    System.out.println(e.fillInStackTrace());
                    System.out.println(str);
                }

            }
            tempMatchMap.put((long) i,getMatchValue(kwMap,map));
            map.clear();
        }
        removeZeroItem(tempMatchMap);
        return hashMapSort(tempMatchMap);
    }
    private double getMatchValue(HashMap<String,Double> kwMap,HashMap<String,Double> map)
    {
        Set<String> keywordsSet = kwMap.keySet();
        double matchValue = 0;
        for(String key:map.keySet())
        {
            if(keywordsSet.contains(key))
            {
                matchValue+=kwMap.get(key)*map.get(key);
            }
        }
        return matchValue;
    }
    private void removeZeroItem(Map<Long, Double> map)
    {
        Iterator<Map.Entry<Long, Double>> it = map.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<Long, Double> entry = it.next();
            if (entry.getValue().equals(new Double(0)))
                it.remove();//使用迭代器的remove()方法删除元素
        }
    }

    public static HashMap<Long, Double>  hashMapSort(HashMap<Long, Double> map){
        //1、按顺序保存map中的元素，使用LinkedList类型
        List<Map.Entry<Long, Double>> keyList = new LinkedList<Map.Entry<Long, Double>>(map.entrySet());
        //2、按照自定义的规则排序
        Collections.sort(keyList, new Comparator<Map.Entry<Long, Double>>() {
            @Override
            public int compare(Map.Entry<Long, Double> o1,
                               Map.Entry<Long, Double> o2) {
                if(o2.getValue().compareTo(o1.getValue())>0){
                    return 1;
                }else if(o2.getValue().compareTo(o1.getValue())<0){
                    return -1;
                }  else {
                    return 0;
                }
            }
        });
        //如果结果大于6，去除后面的
        int flag=keyList.size();
        if(flag>6)
            for(int i=6;i<flag;++i)
                ((LinkedList<Map.Entry<Long,Double>>) keyList).removeLast();
        //3、将LinkedList按照排序好的结果，存入到HashMap中
        HashMap<Long,Double> result=new HashMap<Long,Double>();
        for(Map.Entry<Long, Double> entry:keyList){
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }
}
