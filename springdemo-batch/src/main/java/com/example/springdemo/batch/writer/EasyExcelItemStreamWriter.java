package com.example.springdemo.batch.writer;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 基于EasyExcel的流式Excel写入器
 * 直接写入HTTP响应流，无需生成临时文件
 */
public class EasyExcelItemStreamWriter<T> implements ItemStreamWriter<T> {

    private final HttpServletResponse response;
    private final Class<T> entityClass;
    private final String sheetName;
    private ExcelWriter excelWriter;
    private WriteSheet writeSheet;
    private boolean isInitialized = false;

    public EasyExcelItemStreamWriter(HttpServletResponse response, Class<T> entityClass, String sheetName) {
        this.response = response;
        this.entityClass = entityClass;
        this.sheetName = sheetName;
    }

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

    @Override
    public void write(@SuppressWarnings("null") List<? extends T> items) throws Exception {
        if (!isInitialized) {
            throw new IllegalStateException("Excel写入器未初始化，请先调用open方法");
        }

        if (items != null && !items.isEmpty()) {
            excelWriter.write(items, writeSheet);
        }
    }
}