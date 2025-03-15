package com.example.springdemo.batch.reader;

import com.example.springdemo.batch.model.User;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;

/**
 * 用户CSV文件读取器
 */
public class UserCsvReader extends FlatFileItemReader<User> {

    public UserCsvReader(Resource resource) {
        setResource(resource);
        setLinesToSkip(1); // 跳过CSV文件的标题行
        setLineMapper(createLineMapper());
    }

    private DefaultLineMapper<User> createLineMapper() {
        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<>();
        
        // 设置分隔符和列名
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("username", "password", "email", "phone", "realName", "age", "gender", "address", "role");
        
        // 设置字段映射
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        return lineMapper;
    }
} 