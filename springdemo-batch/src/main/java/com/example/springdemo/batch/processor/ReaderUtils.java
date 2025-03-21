package com.example.springdemo.batch.processor;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReaderUtils {
    @Autowired
    private EntityManagerFactory entityManagerFactory;

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

}
