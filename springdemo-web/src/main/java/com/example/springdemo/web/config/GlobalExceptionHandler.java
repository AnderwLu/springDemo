package com.example.springdemo.web.config;

import com.example.springdemo.common.result.Result;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletResponse;
import java.rmi.server.ExportException;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 处理导出Excel过程中的异常
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(ExportException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleExportException(ExportException e, HttpServletResponse response) {
        log.error("导出Excel异常", e);
        // 如果响应已经提交，则无法再修改
        if (!response.isCommitted()) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
        }
        return Result.error(e.getMessage());
    }
    
    /**
     * 处理文件上传大小超限异常
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件上传大小超限", e);
        return Result.error("上传文件过大");
    }
    
    /**
     * 处理运行时异常
     */
    @SuppressWarnings("rawtypes")

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return Result.error("系统异常: " + e.getMessage());
    }
    
    /**
     * 处理所有其他异常
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统异常: " + e.getMessage());
    }
}