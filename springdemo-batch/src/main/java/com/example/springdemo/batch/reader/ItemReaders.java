package com.example.springdemo.batch.reader;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class ItemReaders {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    private final String delimiter = ",";
    private final int linesToSkip = 1;

    /**
     * 创建JpaPagingItemReader
     */
    public <T> JpaPagingItemReader<T> createUserExportReader(
            String queryBuilder, Map<String, Object> parameterValues, String name) throws Exception {
        // 创建JpaPagingItemReader
        JpaPagingItemReader<T> reader = new JpaPagingItemReader<>();
        reader.setName(name);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(queryBuilder.toString());
        reader.setParameterValues(parameterValues);
        reader.setPageSize(100);

        // 初始化reader
        reader.afterPropertiesSet();

        return reader;
    }
    public <T> FlatFileItemReader<T> createUserCsvReader(String filePath, Class<T> clazz,String[] names) {
        return createCsvReader(clazz, filePath, names, delimiter, linesToSkip, clazz);
    }

    public <T> FlatFileItemReader<T> createCsvReader(Class<T> clazz,String filePath,String[] names,String delimiter,int linesToSkip,Class<T> targetType) {
        FlatFileItemReader<T> reader = new FlatFileItemReader<>();
        
        // 设置资源
        reader.setResource(new FileSystemResource(filePath));
        
        // 设置行映射器
        DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
        
        // 配置分隔符
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(names); // 根据您的用户模型调整字段
        tokenizer.setDelimiter(delimiter);
        lineMapper.setLineTokenizer(tokenizer);
        
        // 配置字段映射
        BeanWrapperFieldSetMapper<T> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(targetType); // 需要强制类型转换
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        // 设置映射器
        reader.setLineMapper(lineMapper);
        
        // 跳过标题行
        reader.setLinesToSkip(linesToSkip);
        
        // 设置名称
        reader.setName(targetType.getSimpleName() + "CsvReader");
        
        return reader;
    }
}
