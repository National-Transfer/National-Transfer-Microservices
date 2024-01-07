package com.ensa.batchservice;



import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BatchServiceApplication {

	private final JobLauncher jobLauncher;
	private final Job job;
	public static void main(String[] args) {
		SpringApplication.run(BatchServiceApplication.class, args);
	}

	@Scheduled(cron = "0 0 23 * * ?")
	public void runJob() throws Exception {
		jobLauncher.run(job, new JobParameters());
	}

}
