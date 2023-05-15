package cn.bsy.cloud.common.mvc.advice;


import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.core.vo.Result;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author gaoh
 * @desc 全局异常捕获处理
 * @date 2022年01月08日 下午 10:34
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    String unauthorizedException = "UnauthorizedException";

    @ExceptionHandler(value = Exception.class)
    public Result<String> handlerCommerceException(Exception ex) {
        log.error("执行异常", ex);
        // web方法参数校验异常
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getBindingResult().getFieldError().getDefaultMessage());
        }
        // 平台自定义异常
        else if (ex instanceof CustomizeException) {
            CustomizeException exception = (CustomizeException) ex;
            return Result.error(exception.getCode(),
                    exception.getMsg());
        }
        // 数据绑定异常处理
        else if (ex instanceof BindException) {
            BindException exception = (BindException) ex;
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exception.getBindingResult().getFieldError().getDefaultMessage());
        }
        // 路径参数异常处理
        else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException exception = (ConstraintViolationException) ex;
            String exceptionMessage = "";
            for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
                exceptionMessage = constraintViolation.getMessage();
                if(ObjectUtil.isNotEmpty(exceptionMessage)){
                    break;
                }
            }
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    exceptionMessage);
        }
        // 权限异常
        else if (StrUtil.equals(ex.getClass().getSimpleName(), unauthorizedException)) {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "无权访问");
        } else {
            return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务端执行异常");
        }
    }
}