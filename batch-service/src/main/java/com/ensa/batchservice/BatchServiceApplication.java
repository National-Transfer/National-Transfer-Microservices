package com.ensa.batchservice;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class BatchServiceApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BatchServiceApplication.class, args);
//		JobLauncher jobLauncher = context.getBean(JobLauncher.class);
//		Job job = context.getBean("importUserJob", Job.class);
//
//		try {
//			JobExecution execution = jobLauncher.run(job, new JobParameters());
//			System.out.println("Job Status : " + execution.getStatus());
//		} catch (JobExecutionException e) {
//			e.printStackTrace();
//		}
	}

}
