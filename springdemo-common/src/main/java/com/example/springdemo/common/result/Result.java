package com.example.springdemo.common.result;

import lombok.Data;

/**
 * 通用返回结果
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功结果
     *
     * @param <T> 数据类型
     * @return 结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败结果
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return 结果
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 失败结果
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     数据类型
     * @return 结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败结果
     *
     * @param errorCode 错误码
     * @param <T>       数据类型
     * @return 结果
     */
    public static <T> Result<T> error(IErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }
} 