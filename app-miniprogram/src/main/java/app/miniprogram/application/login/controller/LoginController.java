package app.miniprogram.application.login.controller;

import app.miniprogram.security.service.WxAccountService;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :wkh.
 * @date :2019/8/30.
 */
@RestController
@RequestMapping("api/login")
@Slf4j
public class LoginController {

    @Autowired
    private WxAccountService accountService;

    @RequestMapping("/token")
    public ResponseEntity<JsonResult> wxLogin(@Param("code") String code) {
        try {
            log.info("token获取开始");
            return new ResponseEntity<>(JsonResult.success(accountService.getWxLoginToken(code)), HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("获取token失败");
            return new ResponseEntity<>(JsonResult.failed("获取token失败"), HttpStatus.OK);
        }
    }
}
