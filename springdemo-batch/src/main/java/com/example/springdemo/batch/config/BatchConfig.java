package com.example.springdemo.batch.config;

import com.example.springdemo.batch.listener.JobCompletionNotificationListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch配置类
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobCompletionNotificationListener listener;

    private <I,O> Step fileImportJpaStep(ItemStreamReader<I> reader, ItemProcessor<I,O> processor, JpaItemWriter<O> writer) {
        return new StepBuilder("fileImportDbStep", jobRepository)
                .<I,O>chunk(10, transactionManager) // 每次处理10条记录
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * 创建文件导入JPA作业
     * @param reader 数据读取器
     * @param processor 数据处理器
     * @param writer 数据写入器
     * @return 创建文件导入JPA作业
     */
    public <I,O> Job fileImportJpaJob(ItemStreamReader<I> reader, ItemProcessor<I,O> processor, JpaItemWriter<O> writer) {
        return new JobBuilder("fileImportDbJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(fileImportJpaStep(reader, processor, writer))
                .end()
                .build();
    }

    private <I,O> Step jpaImportFileStep(JpaPagingItemReader<I> reader, ItemProcessor<I,O> processor, ItemStreamWriter<O> writer) {
        return new StepBuilder("jpaImportFileStep", jobRepository)
                .<I,O>chunk(10, transactionManager) // 每次处理10条记录
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * 创建JPA导出文件作业
     * @param reader 数据读取器
     * @param processor 数据处理器
     * @param writer 数据写入器
     * @return 创建JPA导出文件作业
     */
    public <I,O> Job jpaImportFileJob(JpaPagingItemReader<I> reader, ItemProcessor<I,O> processor, ItemStreamWriter<O> writer) {
        return new JobBuilder("jpaImportFileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(jpaImportFileStep(reader, processor, writer))
                .end()
                .build();
    }

    private <I,O> Step fileImportFileStep(ItemStreamReader<I> reader, ItemProcessor<I,O> processor, ItemStreamWriter<O> writer) {
        return new StepBuilder("fileImportFileStep", jobRepository)
                .<I,O>chunk(10, transactionManager) // 每次处理10条记录
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * 创建文件导入文件作业
     * @param reader 数据读取器
     * @param processor 数据处理器
     * @param writer 数据写入器
     * @return 创建文件导入文件作业
     */
    public <I,O> Job fileImportFileJob(ItemStreamReader<I> reader, ItemProcessor<I,O> processor, ItemStreamWriter<O> writer) {
        return new JobBuilder("fileImportFileJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(fileImportFileStep(reader, processor, writer))
                .end()
                .build();
    }
} 