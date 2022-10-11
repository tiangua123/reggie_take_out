package com.itheima.reggie.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})

public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @param ex
     * @return
     */

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){

        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String msg=s[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){

        return R.error(ex.getMessage());
    }
}
