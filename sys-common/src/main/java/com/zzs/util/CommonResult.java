package com.zzs.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mm013
 * @Date 2021/5/11 17:08
 */
@Data
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    /**
     * 成功
     */
    public static final int CODE_SUCCESS = 200;

    /**
     * 空指针异常
     */
    public static final int CODE_COMMON_ERROR = 400;

    /**
     * 未知错误（服务器内部错误，前端看情况提示）
     */
    public static final int CODE_UNKNOWN_ERROR = 500;

    public static final String SUCCESS_MESSAGE = "操作成功";


    public static final String MESSAGE_UNKNOWN_ERROR = "未知错误，请联系开发人员";

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功
     *
     * @param data 返回数据
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult(CODE_SUCCESS, SUCCESS_MESSAGE, data);
    }

    /**
     * 成功，无返回
     */
    public static <T> CommonResult<T> success(String success) {
        return new CommonResult(CODE_SUCCESS, success);
    }

    /**
     * 空指针失败消息
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult(CODE_COMMON_ERROR, message, null);
    }

    /**
     * 未知错误失败
     *
     * @param code    出错码
     * @param message 出错消息
     */
    public static <T> CommonResult<T> error(int code, String message) {
        return new CommonResult<>(code, message, null);
    }

    /**
     * 未知错误
     */
    public static <T> CommonResult<T> UNKNOWN_ERROR() {
        return error(CODE_UNKNOWN_ERROR, MESSAGE_UNKNOWN_ERROR);
    }
}
