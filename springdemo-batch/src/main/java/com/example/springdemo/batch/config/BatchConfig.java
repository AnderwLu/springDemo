package com.example.springdemo.batch.config;

import com.example.springdemo.batch.listener.JobCompletionNotificationListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
    private JobCompletionNotificationListener listener;

    private <I,O> Step fileImportJpaStep(ItemStreamReader<I> reader, ItemProcessor<I,O> processor, JpaItemWriter<O> writer) {
        return stepBuilderFactory.get("fileImportDbStep")
                .<I,O>chunk(10) // 每次处理10条记录
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
        return jobBuilderFactory.get("fileImportDbJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(fileImportJpaStep(reader, processor, writer))
                .end()
                .build();
    }

    private <I,O> Step jpaImportFileStep(JpaPagingItemReader<I> reader, ItemProcessor<I,O> processor, ItemStreamWriter<O> writer) {
        return stepBuilderFactory.get("jpaImportFileStep")
                .<I,O>chunk(10) // 每次处理10条记录
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
        return jobBuilderFactory.get("jpaImportFileJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(jpaImportFileStep(reader, processor, writer))
                .end()
                .build();
    }

    private <I,O> Step fileImportFileStep(ItemStreamReader<I> reader, ItemProcessor<I,O> processor, ItemStreamWriter<O> writer) {
        return stepBuilderFactory.get("fileImportFileStep")
                .<I,O>chunk(10) // 每次处理10条记录
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
        return jobBuilderFactory.get("fileImportFileJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(fileImportFileStep(reader, processor, writer))
                .end()
                .build();
    }

} 