package com.yjs.springdemo.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Date;

/**
 * Created by yjs on 2018/5/22.
 */
@Slf4j
public class MyJob_DataMap implements org.quartz.Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(getClass().getName()+" is running:"+ new Date());

        JobKey key = jobExecutionContext.getJobDetail().getKey();

        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String jobsays = dataMap.getString("Jobsays");
        Float jobFloat = dataMap.getFloat("JobFloatvalue");
        String who = dataMap.getString("Who");
        Integer old = dataMap.getInt("Mapvalue");
        log.info("Instance " + key + " of " + getClass().getSimpleName()+" says: "+jobsays +",jobFloat="+jobFloat+" ,"+who +" is "+old +" year old");

    }
}