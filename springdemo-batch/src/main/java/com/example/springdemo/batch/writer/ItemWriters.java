package com.example.springdemo.batch.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ItemWriters {
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    /**
     * 创建一个JPA ItemWriter
     * @param <T> 数据项类型
     * @return 配置好的JpaItemWriter
     */
    public <T> JpaItemWriter<T> createJpaItemWriter() {
        JpaItemWriter<T> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
    /**
     * 创建平面文件写入器
     * @param <T> 数据项类型
     * @param filePath 文件路径
     * @param delimiter 分隔符
     * @param names 字段名数组
     * @return 配置好的FlatFileItemWriter
     */
    public <T> FlatFileItemWriter<T> createFlatFileItemWriter(String filePath, String delimiter, String[] names) {
        FlatFileItemWriter<T> writer = new FlatFileItemWriter<>();
        
        // 设置输出资源
        writer.setResource(new FileSystemResource(filePath));
        
        // 设置行聚合器 - 将对象转换为字符串
        DelimitedLineAggregator<T> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(delimiter);
        
        // 设置字段提取器 - 从对象中提取字段
        BeanWrapperFieldExtractor<T> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(names);
        
        // 设置字段提取器到行聚合器
        lineAggregator.setFieldExtractor(fieldExtractor);
        
        // 设置行聚合器到写入器
        writer.setLineAggregator(lineAggregator);
        
        // 设置编码（可选，默认为UTF-8）
        writer.setEncoding("UTF-8");
        
        return writer;
    }
    /**
     * 创建基于EasyExcel的流式Excel写入器
     * @param response 响应对象，用于输出Excel文件
     * @param entityClass 实体类类型
     * @param sheetName 工作表名称
     * @return 基于EasyExcel的流式Excel写入器
     */
    public <T> ItemStreamWriter<T> createEasyExcelItemStreamWriter(HttpServletResponse response, Class<T> entityClass, String sheetName) {
        return new ItemStreamWriter<T>() {
            @Override
            public void write(Chunk<? extends T> chunk) throws Exception {
                if (!isInitialized) {
                    throw new IllegalStateException("Excel写入器未初始化，请先调用open方法");
                }

                if (chunk != null && !chunk.isEmpty()) {
                    excelWriter.write(chunk.getItems(), writeSheet);
                }
            }

            private ExcelWriter excelWriter;
            private WriteSheet writeSheet;
            private boolean isInitialized = false;
            @Override
            public void open(@SuppressWarnings("null") ExecutionContext executionContext) throws ItemStreamException {
                try {
                    // 创建ExcelWriter，配置样式
                    OutputStream outputStream = response.getOutputStream();
                    excelWriter = EasyExcel.write(outputStream, entityClass)
                            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                            .build();
        
                    // 创建写入的Sheet
                    writeSheet = EasyExcel.writerSheet(sheetName).build();
                    isInitialized = true;
                } catch (IOException e) {
                    throw new ItemStreamException("无法创建Excel写入器", e);
                }
            }
        
            @Override
            public void update(@SuppressWarnings("null") ExecutionContext executionContext) throws ItemStreamException {
                // 可以在这里更新执行上下文中的状态信息
            }
        
            @Override
            public void close() throws ItemStreamException {
                // 关闭ExcelWriter
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        };
    }

    /**
     * 创建基于EasyExcel的流式Excel写入器
     * @param filePath 文件路径
     * @param entityClass 实体类类型
     * @param sheetName 工作表名称
     * @return 基于EasyExcel的流式Excel写入器
     */
    public <T> ItemStreamWriter<T> createEasyExcelItemStreamWriter(String filePath, Class<T> entityClass, String sheetName) {
        return new ItemStreamWriter<T>() {
            @Override
            public void write(Chunk<? extends T> chunk) throws Exception {
                if (!isInitialized) {
                    throw new IllegalStateException("Excel写入器未初始化，请先调用open方法");
                }

                if (chunk != null && !chunk.isEmpty()) {
                    excelWriter.write(chunk.getItems(), writeSheet);
                }
            }

            private ExcelWriter excelWriter;
            private WriteSheet writeSheet;
            private boolean isInitialized = false;
            @Override
            public void open(@SuppressWarnings("null") ExecutionContext executionContext) throws ItemStreamException {
                try {
                    // 创建ExcelWriter，配置样式
                    excelWriter = EasyExcel.write(filePath, entityClass)
                            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                            .build();
        
                    // 创建写入的Sheet
                    writeSheet = EasyExcel.writerSheet(sheetName).build();
                    isInitialized = true;
                } catch (Exception e) {
                    throw new ItemStreamException("无法创建Excel写入器", e);
                }
            }

        
            @Override
            public void update(@SuppressWarnings("null") ExecutionContext executionContext) throws ItemStreamException {
                // 可以在这里更新执行上下文中的状态信息
            }
        
            @Override
            public void close() throws ItemStreamException {
                // 关闭ExcelWriter
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        };
    }
}
