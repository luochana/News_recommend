package com.luochen.recommend.ContentBaseRecommender.quartz;

import com.luochen.web.Dao.interf.NewsKeywordsRepository;
import com.luochen.web.Dao.interf.NewsRepository;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

public class calculateKeyWordsTriggerRunner {
    public void task(NewsRepository newsRepository, NewsKeywordsRepository newsKeywordsRepository) throws SchedulerException
    {
        SchedulerFactory schedulerFactory=new StdSchedulerFactory();
        Scheduler scheduler=schedulerFactory.getScheduler();
        JobDetailImpl jobDetail=new JobDetailImpl();
        jobDetail.setJobClass(calculateKeywordsJobs.class);
        jobDetail.setKey(new JobKey("calculateKeywordsJobs"));
        jobDetail.getJobDataMap().put("newsRepo",newsRepository);
        jobDetail.getJobDataMap().put("newsKwdRepo",newsKeywordsRepository);

        CronTriggerImpl cronTriggerImpl=new CronTriggerImpl();
        cronTriggerImpl.setName("caculateKwdCronTrigger1");

        try
        {
            CronExpression cexp=new CronExpression("0 50 19 * * ?");
            cronTriggerImpl.setCronExpression(cexp);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        scheduler.scheduleJob(jobDetail,cronTriggerImpl);
        scheduler.start();
    }
}
