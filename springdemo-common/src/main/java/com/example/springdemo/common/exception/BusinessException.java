package com.example.springdemo.common.exception;

import com.example.springdemo.common.result.IErrorCode;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 构造函数
     *
     * @param message 错误信息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造函数
     *
     * @param message 错误信息
     * @param code    错误码
     */
    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码对象
     */
    public BusinessException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }
} 