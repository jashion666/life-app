package app.miniprogram.application.recognition.controller;

import app.miniprogram.application.recognition.service.RecognitionService;
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
import org.springframework.web.multipart.MultipartFile;

/**
 * @author :wkh.
 * @date :2019/9/3.
 */
@RestController
@RequestMapping("recognition")
@Slf4j
public class RecognitionController {

    private final RecognitionService recognitionService;

    @RequestMapping("test")
    public String test(@RequestParam("file") MultipartFile file) {
        return file.getName();
    }

    @RequestMapping("scan")
    @RequiresAuthentication
    public ResponseEntity<JsonResult> recognitionImg(@RequestParam("file") MultipartFile file,
                                                     @RequestParam(value = "languageType", required = false) String languageType) {
        try {
            return new ResponseEntity<>(
                    JsonResult.success(recognitionService.extractText(file.getInputStream(), languageType)),
                    HttpStatus.OK);
        } catch (AppException e) {
            return new ResponseEntity<>(JsonResult.failed(e.getMessage()), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(JsonResult.failed("识别失败了--!"), HttpStatus.OK);
    }

    @Autowired
    public RecognitionController(RecognitionService recognitionService) {
        this.recognitionService = recognitionService;
    }
}
