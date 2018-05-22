package com.yjs.springdemo.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by yjs on 2018/5/21.
 */
@Slf4j
public class MyJob implements org.quartz.Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("MyJob is running:"+ new Date());
    }
}
