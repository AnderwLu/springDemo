package com.example.springdemo.batch.config;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.springdemo.dao.entity.user.User;
import com.example.springdemo.dao.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.WriteFailedException;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 用户数据导出批处理配置
 */
@Configuration
public class ExportBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * 文件导出路径
     */
    private String exportFilePath = System.getProperty("java.io.tmpdir") + "/users_export.xlsx";

    /**
     * 配置从数据库读取用户数据的reader
     * 支持动态条件查询，使用JpaPagingItemReader
     */
    @Bean
    @StepScope
    public JpaPagingItemReader<User> userExportReader(
            @Value("#{jobParameters['username']}") String username,
            @Value("#{jobParameters['email']}") String email,
            @Value("#{jobParameters['startDate']}") String startDate,
            @Value("#{jobParameters['endDate']}") String endDate) throws Exception {
        
        // 构建动态查询JPQL
        StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u WHERE 1=1");
        Map<String, Object> parameterValues = new HashMap<>();
        
        // 如果提供了用户名参数，添加用户名模糊查询条件
        // 使用LOWER函数确保大小写不敏感的匹配
        if (username != null && !username.isEmpty()) {
            queryBuilder.append(" AND LOWER(u.username) LIKE LOWER(:username)");
            parameterValues.put("username", "%" + username + "%"); // 添加百分号实现模糊查询
        }
        
        if (email != null && !email.isEmpty()) {
            queryBuilder.append(" AND LOWER(u.email) LIKE LOWER(:email)");
            parameterValues.put("email", "%" + email + "%");
        }
        
        // 如果有日期范围条件，需要确保User实体有相应字段(如createTime)
        if (startDate != null && !startDate.isEmpty()) {
            queryBuilder.append(" AND u.createTime >= :startDate");
            parameterValues.put("startDate", startDate);
        }
        
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            queryBuilder.append(" AND u.createTime BETWEEN :startDate AND :endDate");
            parameterValues.put("startDate", startDate);
            parameterValues.put("endDate", endDate);
        } else if (endDate != null && !endDate.isEmpty()) {
            queryBuilder.append(" AND u.createTime <= :endDate");
            parameterValues.put("endDate", endDate);
        }
        
        // 排序
        queryBuilder.append(" ORDER BY u.id ASC");
        
        // 构建JpaPagingItemReader - 不使用Builder模式
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        reader.setName("userExportReader");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(queryBuilder.toString());
        reader.setParameterValues(parameterValues);
        reader.setPageSize(100);
        
        // 初始化reader
        reader.afterPropertiesSet();
        
        return reader;
    }

    /**
     * 用户数据处理器
     * 主要用于在导出前处理敏感数据
     */
    @Bean
    public ItemProcessor<User, User> userExportProcessor() {
        return user -> {
            // 不导出密码等敏感信息
            user.setPassword("[PROTECTED]");
            return user;
        };
    }

    /**
     * 配置导出到Excel的writer，使用ItemStreamWriter接口
     * 确保在不同批次之间正确处理ExcelWriter的生命周期
     */
    @Bean
    @StepScope
    public ItemStreamWriter<User> userExportWriter(@Value("#{jobParameters['outputPath']}") String outputPath) {
        // 如果提供了输出路径，则使用提供的路径
        final String filePath = (outputPath != null && !outputPath.isEmpty()) 
                ? outputPath 
                : this.exportFilePath;
        
        return new ItemStreamWriter<User>() {
            private ExcelWriter excelWriter;
            private WriteSheet writeSheet;
            
            @Override
            public void open(org.springframework.batch.item.ExecutionContext executionContext) throws ItemStreamException {
                try {
                    // 确保导出目录存在
                    Path path = Paths.get(filePath);
                    if (!Files.exists(path.getParent())) {
                        Files.createDirectories(path.getParent());
                    }
                    
                    // 创建ExcelWriter，只创建一次
                    excelWriter = EasyExcel.write(filePath, User.class).build();
                    writeSheet = EasyExcel.writerSheet("用户数据").build();
                } catch (IOException e) {
                    throw new ItemStreamException("无法创建导出文件", e);
                }
            }
            
            @Override
            public void update(org.springframework.batch.item.ExecutionContext executionContext) throws ItemStreamException {
                // 可以在这里更新处理状态
            }
            
            @Override
            public void close() throws ItemStreamException {
                // 关闭ExcelWriter，确保所有数据写入并释放资源
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
            
            @Override
            public void write(List<? extends User> items) throws Exception {
                if (excelWriter != null && writeSheet != null) {
                    excelWriter.write(items, writeSheet);
                } else {
                    throw new WriteFailedException("ExcelWriter未初始化");
                }
            }
        };
    }

    /**
     * 配置导出步骤
     */
    @Bean
    public Step exportUserStep() throws Exception {
        return stepBuilderFactory.get("exportUserStep")
                .<User, User>chunk(100)
                .reader(userExportReader(null, null, null, null))
                .processor(userExportProcessor())
                .writer(userExportWriter(null))
                .build();
    }

    /**
     * 配置导出作业
     */
    @Bean
    public Job exportUserJob() throws Exception {
        return jobBuilderFactory.get("exportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(exportUserStep())
                .end()
                .build();
    }

    /**
     * 获取导出文件路径
     */
    public String getExportFilePath() {
        return exportFilePath;
    }

    /**
     * 设置导出文件路径
     */
    public void setExportFilePath(String exportFilePath) {
        this.exportFilePath = exportFilePath;
    }
}