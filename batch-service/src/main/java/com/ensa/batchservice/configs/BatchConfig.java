package com.ensa.batchservice.configs;


import com.ensa.batchservice.dto.TransferDto;
import com.ensa.batchservice.services.TransferProcessor;
import com.ensa.batchservice.services.TransferReader;
import com.ensa.batchservice.services.TransferWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;



@RequiredArgsConstructor

@Configuration
@EnableRetry
public class BatchConfig {


    private final TransferReader transferReader;
    private final TransferProcessor transferProcessor;
    private final TransferWriter transferWriter;
    private final JobLauncher jobLauncher;


    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    @Retryable
    public Step step1(JobRepository jobRepository, TransferReader reader, TransferProcessor processor, TransferWriter writer, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<TransferDto, TransferDto> chunk(3,transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }


}
