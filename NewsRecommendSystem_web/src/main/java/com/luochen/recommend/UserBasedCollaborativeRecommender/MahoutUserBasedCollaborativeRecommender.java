package com.luochen.recommend.UserBasedCollaborativeRecommender;

import com.luochen.tools.RecommKits;
import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.NewsLogsRepository;
import com.luochen.web.Dao.interf.RecommendationsRepository;
import com.luochen.web.Dao.newsLogs;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLBooleanPrefJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class MahoutUserBasedCollaborativeRecommender {
    //计算相似度的时效天数
    private static int inRecDays=1;
    //给每个用户推荐的新闻的条数
    public static int N=10;

    //***设置为参数

/*    @Autowired
    private NewsLogsRepository newsLogsRepository;
    @Autowired
    private NewsKeywordsRepository newsKeywordsRepository;
    @Autowired
    private RecommendationsRepository recommendationsRepository;*/
    public Set<Long> recommend(List<Long> users,NewsLogsRepository newsLogsRepository,NewsKeywordsRepository newsKeywordsRepository, RecommendationsRepository recommendationsRepository)
    {
        int count=0;
        Set<Long> hs = new HashSet<Long>();
        try
        {
            MysqlDataSource dataSource= GetDbSource.getDataSource();
            MySQLBooleanPrefJDBCDataModel dataModel = new MySQLBooleanPrefJDBCDataModel(dataSource,"news_logs","user_id","news_id","view_time");
            List<newsLogs> newsLogsList= new ArrayList<newsLogs>();
            newsLogsList= newsLogsRepository.findAllRecord();
            for(newsLogs newsLog:newsLogsList)
            {
                if(newsLog.getView_time().before(RecommKits.getInRecTimestamp(inRecDays)))
                    dataModel.removePreference(newsLog.getUser_id(),newsLog.getNews_id());
            }
            UserSimilarity similarity = new LogLikelihoodSimilarity(dataModel);
            // NearestNeighborhood的数量有待考察
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, dataModel);
            Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            for (Long user : users)
            {
                Long userid = user;

                List<RecommendedItem> recItems = recommender.recommend(userid, N);

              //  Set<Long> hs = new HashSet<Long>();

                for (RecommendedItem recItem : recItems)
                {
                    hs.add(recItem.getItemID());
                }
                RecommKits.filterOutDateNews(hs,userid,newsKeywordsRepository);
                RecommKits.filterReccedNews(hs,userid,recommendationsRepository);
                if(hs.isEmpty())
                    continue;
                if(hs.size()>N)
                    RecommKits.removeOverNews(hs,N);
                RecommKits.insertRecommend(userid,hs.iterator(),recommendationsRepository);
                count+=hs.size();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hs;
    }
}
