package com.example.springdemo.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户数据处理器
 */
@Slf4j
@Component
public class Processors {

    /**
     * 获取默认处理器
     * @param <T>
     * @return
     */
    public <T> ItemProcessor<T,T> getDefaultProcessor() {
        return item -> item;
    }
} 