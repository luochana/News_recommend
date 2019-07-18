package com.luochen.recommend.ContentBaseRecommender.quartz;

import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.NewsRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static com.luochen.recommend.ContentBaseRecommender.calculateKeywords.makeKeywords;

public class calculateKeywordsJobs implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       // NewsRepository newsRepository= (NewsRepository) jobExecutionContext.getJobDetail().getJobDataMap().get("newsRepo");
        makeKeywords((NewsRepository)jobExecutionContext.getJobDetail().getJobDataMap().get("newsRepo"),
                (NewsKeywordsRepository)jobExecutionContext.getJobDetail().getJobDataMap().get("newsKwdRepo"));
    }
}
