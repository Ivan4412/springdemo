package com.yjs.springdemo;

import com.yjs.springdemo.jobs.MyJob;
import com.yjs.springdemo.jobs.MyJob_DataMap;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.CronCalendar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@SpringBootApplication
public class QuartzApplication {

	public static void main(String[] args) {

		SpringApplication.run(QuartzApplication.class, args);
		try {
			startJob3();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public static void startJob() throws SchedulerException {

		//创建scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		//定义JobDetail
		JobDetail job = newJob(MyJob.class)
				.withIdentity("job1","group1")
				.build();

		//定义trigger
		Trigger trigger = newTrigger()
				.withIdentity("trigger","group1")
				.startNow() //一旦加入scheduler，立即生效
				.withSchedule(simpleSchedule() //使用SimpleTrigger
						.withIntervalInSeconds(5) //每隔5秒执行一次
						.repeatForever()) //一直执行下去
				.build();

		//加入调度
		scheduler.scheduleJob(job,trigger);
		//启动
		scheduler.start();
/*		//运行一段时间后关闭
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		scheduler.shutdown(true);*/
	}

	public static void startJob2() throws SchedulerException {

		//创建scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		//定义JobdataMap值
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("Mapvalue",10);
		dataMap.put("Who","ivan");

		//定义JobDetail
		JobDetail job = newJob(MyJob_DataMap.class)
				.withIdentity("job2","group1")
				.usingJobData("Jobsays","hello I'm coming")                //Note the difference
				.usingJobData("JobFloatvalue",3.14159f)    //Note the difference
				.usingJobData(dataMap)   //Note the difference
				.build();

		//定义trigger
		Trigger trigger = newTrigger()
				.withIdentity("trigger","group1")
				.startNow() //一旦加入scheduler，立即生效
				.withSchedule(simpleSchedule() //使用SimpleTrigger
						.withIntervalInSeconds(40) //每隔40秒执行一次
						.repeatForever()) //一直执行下去
				.build();

		//加入调度
		scheduler.scheduleJob(job,trigger);
		//启动
		scheduler.start();
	}

	public static void startJob3() throws SchedulerException {

		//创建scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		//定义JobdataMap值
		JobDataMap dataMap = new JobDataMap();
		dataMap.put("Mapvalue",10);
		dataMap.put("Who","ivan");

		//定义JobDetail
		JobDetail job = newJob(MyJob_DataMap.class)
				.withIdentity("job3","group1")
				.usingJobData("Jobsays","hello I'm coming")
				.usingJobData("JobFloatvalue",3.14159f)
				.usingJobData(dataMap)
				.build();

		//定义trigger
		Trigger trigger = newTrigger()
				.withIdentity("trigger","group1")
				.startNow() //一旦加入scheduler，立即生效
				.withSchedule(cronSchedule("0 0/2 8-17 * * ?"))   // 每天8:00-17:00，每隔2分钟执行一次  //Note difference
				.build();

		//加入调度
		scheduler.scheduleJob(job,trigger);
		//启动
		scheduler.start();
	}
}

