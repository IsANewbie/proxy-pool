package com.vtech.newscrawler.job.execute.impl;

import com.vtech.newscrawler.job.execute.ISchedulerJobExecutor;
import com.vtech.newscrawler.job.scheduler.AbstractSchedulerJob;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author chenerzhu
 * @create 2018-08-30 12:15
 **/
public class SchedulerJobExecutor implements ISchedulerJobExecutor {

    private ScheduledExecutorService scheduledExecutorService;
    public SchedulerJobExecutor(){}

    public SchedulerJobExecutor(String threadFactory){
//        scheduledExecutorService=Executors.newScheduledThreadPool(10,new ThreadFactory(threadFactory));
    }

    public SchedulerJobExecutor(int corePoolSize,String threadFactory){
//        scheduledExecutorService=Executors.newScheduledThreadPool(corePoolSize,new ThreadFactory(threadFactory));
    }


    public void execute(AbstractSchedulerJob schedulerJob, long delayTime, long intervalTime, TimeUnit timeUnit){
//        scheduledExecutorService.scheduleAtFixedRate(schedulerJob,delayTime,intervalTime,timeUnit);
    }
    public void executeDelay(AbstractSchedulerJob schedulerJob, long delayTime, long intervalTime, TimeUnit timeUnit){
//        scheduledExecutorService.scheduleWithFixedDelay(schedulerJob,delayTime,intervalTime,timeUnit);
    }

    public void shutdown(){

//        scheduledExecutorService.shutdown();
    }
}