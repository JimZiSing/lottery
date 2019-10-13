package org.javatribe.lottery.controller;


import lombok.extern.slf4j.Slf4j;
import org.javatribe.lottery.entity.Result;
import org.javatribe.lottery.enums.ResultEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author JimZiSing
 * 统一处理异常类,防止抛出异常给前端
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = Exception.class)
    public Result<String> handler(Exception e) {
        log.error("出现错误："+e.getMessage(),e);
        return Result.error(ResultEnum.SERVER_ERROR);
    }
}
