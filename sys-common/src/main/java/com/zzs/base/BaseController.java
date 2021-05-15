package com.zzs.base;

import com.zzs.util.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常控制
 *
 * @author mm013
 * @Date 2021/5/11 17:38
 */
@ControllerAdvice
public abstract class BaseController {
    /**
     * 默认页码
     */
    protected final static String DEFAULT_PARAM_PAGE_NUM = "1";
    /**
     * 默认分页大小
     */
    protected final static String DEFAULT_PARAM_PAGE_SIZE = "20";

    /**
     * 成功消息
     */
    protected static final String SUCCESS_MESSAGE = "操作成功";

    protected static final String BODY_NOT_MATCH = "请求的数据格式不符!";


    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public CommonResult exceptionHandler(HttpServletRequest req, NullPointerException e) {
        return CommonResult.error(BODY_NOT_MATCH);
    }

    /**
     * 处理全局异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult exceptionHandler(Exception e) {
        System.out.println("未知异常！请告知开发人员:" + e);
        return CommonResult.UNKNOWN_ERROR();
    }
}

