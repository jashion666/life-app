package app.miniprogram.security.exception;

import app.miniprogram.security.exception.model.ExceptionResponse;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :wkh
 * 全局异常 及其自定义异常 返回处理
 */
@ControllerAdvice()
@Slf4j
public class ControllerExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonResult> throwable(Throwable throwable, HttpServletRequest request) {
        ExceptionResponse responseEntity = ExceptionResponse.getCurrentException();
        try {
            responseEntity.setMessage(throwable.getMessage());
            if (throwable instanceof UnauthenticatedException || throwable instanceof AuthenticationException) {
                return new ResponseEntity<>(JsonResult.failedAccessDenied("禁止访问"), HttpStatus.OK);
            }
        } catch (Exception ignore) {
        } finally {
            ExceptionResponse.removeExceptionResponse();
        }
        return new ResponseEntity<>(JsonResult.failed("接口请求失败"), HttpStatus.OK);
    }

}