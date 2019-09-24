package app.miniprogram.security.exception;

import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :wkh
 * 全局异常 及其自定义异常 返回处理
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<JsonResult> throwable(Throwable throwable) {
        log.error(throwable.getMessage());
        return new ResponseEntity<>(JsonResult.failed("接口请求失败"), HttpStatus.OK);
    }

}