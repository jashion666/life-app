package app.miniprogram.application.translate.controller;

import app.miniprogram.application.translate.service.TranslateService;
import app.miniprogram.security.exception.AppException;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :wkh.
 * @date :2019/9/16.
 */
@RestController
@RequestMapping("translate")
@Slf4j
public class TranslateController {

    @Autowired
    private TranslateService translateService;

    @RequestMapping("normal")
    @RequiresAuthentication
    public ResponseEntity<JsonResult> translate(@RequestParam("query") String query,
                                                @RequestParam("from") String beTranslation,
                                                @RequestParam("to") String afterTranslation) {
        try {
            return new ResponseEntity<>(JsonResult.success(translateService.translate(query.trim(), beTranslation, afterTranslation)), HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity<>(JsonResult.failed(e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(JsonResult.failed("翻译失败了--!"), HttpStatus.OK);
    }
}
