package com.example.springdemo.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 作业完成通知监听器
 */
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final JdbcTemplate jdbcTemplate;

    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void beforeJob(@SuppressWarnings("null") JobExecution jobExecution) {
        log.info("开始执行导入作业: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(@SuppressWarnings("null") JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("导入作业完成!");
            
            // 查询导入的用户数量
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
            log.info("数据库中共有 {} 个用户", count);
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("导入作业失败: {}", jobExecution.getExitStatus().getExitDescription());
        }
    }
} 