package com.example.springdemo.batch.config;

import com.example.springdemo.batch.listener.JobCompletionNotificationListener;
import com.example.springdemo.batch.model.User;
import com.example.springdemo.batch.processor.UserItemProcessor;
import com.example.springdemo.batch.reader.UserCsvReader;
import com.example.springdemo.batch.writer.UserItemWriter;
import com.example.springdemo.dao.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

/**
 * Spring Batch配置类
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @Bean
    public FlatFileItemReader<User> reader() {
        return new UserCsvReader(new ClassPathResource("users.csv"));
    }

    @Bean
    public UserItemProcessor processor() {
        return new UserItemProcessor(passwordEncoder);
    }

    @Bean
    public Step importUserStep() {
        return stepBuilderFactory.get("importUserStep")
                .<User, User>chunk(10) // 每次处理10条记录
                .reader(reader())
                .processor(processor())
                .writer(UserItemWriter.createWriter(userRepository))
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(importUserStep())
                .end()
                .build();
    }
} 