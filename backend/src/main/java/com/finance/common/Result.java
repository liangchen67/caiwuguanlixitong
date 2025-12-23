package com.finance.common;

import lombok.Data;

/**
 * 统一API响应结果封装类
 * 
 * <p>用于统一后端接口的返回格式，包含状态码、消息和数据三个部分。</p>
 * 
 * <p>返回格式示例：</p>
 * <pre>{@code
 * {
 *   "code": 200,
 *   "message": "操作成功",
 *   "data": { ... }
 * }
 * }</pre>
 * 
 * <p>使用示例：</p>
 * <pre>{@code
 * // 成功返回
 * return Result.success(userList);
 * 
 * // 失败返回
 * return Result.error("用户不存在");
 * }</pre>
 * 
 * @param <T> 返回数据的类型
 * @author 财务管理系统
 * @version 1.0
 * @since 2025-01-01
 */
@Data
public class Result<T> {
    
    /** HTTP状态码，200表示成功，500表示失败 */
    private Integer code;
    
    /** 返回消息，描述操作结果 */
    private String message;
    
    /** 返回的数据，可以是任意类型 */
    private T data;

    /**
     * 成功返回（无数据）
     * 
     * @param <T> 数据类型
     * @return Result对象，code=200，message="操作成功"
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * 成功返回（带数据）
     * 
     * @param <T> 数据类型
     * @param data 返回的数据
     * @return Result对象，code=200，message="操作成功"
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回（自定义消息和数据）
     * 
     * @param <T> 数据类型
     * @param message 自定义消息
     * @param data 返回的数据
     * @return Result对象，code=200
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败返回（默认状态码500）
     * 
     * @param <T> 数据类型
     * @param message 错误消息
     * @return Result对象，code=500
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回（自定义状态码）
     * 
     * @param <T> 数据类型
     * @param code 自定义状态码
     * @param message 错误消息
     * @return Result对象
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}










