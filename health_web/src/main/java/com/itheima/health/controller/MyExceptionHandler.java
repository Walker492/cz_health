package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 统一异常处理
 * 【注意】，这个类必须进入spring容器，要在扫包的目录下即可
 * </p>
 * 相当try
 * @author: Eric
 * @since: 2020/10/24
 */
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * info: 记录执行的过程
     * debug: 记录执行过程中重要的key
     * error: 记录异常信息
     */
    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    /**
     * 捕获异常
     * catch(MyExcetion e)
     */
    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        return new Result(false, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        // e.printStackTrace(); System.out.println() // out输出流 硬件输出设备 占用大量系统资源
        log.error("发生未知异常",e);
        return new Result(false, "发生未知异常，请联系管理员");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        return new Result(false, "没有权限");
    }

}
