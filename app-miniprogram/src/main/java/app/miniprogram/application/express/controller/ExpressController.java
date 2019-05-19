package app.miniprogram.application.express.controller;

import app.miniprogram.utils.JsonResult;
import org.apache.http.client.HttpClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 快递业务接口
 *
 * @author :wkh.
 * @date :2019/5/17.
 */
@RestController
@RequestMapping("express")
public class ExpressController {

    @RequestMapping("list")
    public ResponseEntity<JsonResult> getExpressList() {
        return new ResponseEntity<>(JsonResult.success("第一次调用成功！！！"), HttpStatus.OK);
    }
}
