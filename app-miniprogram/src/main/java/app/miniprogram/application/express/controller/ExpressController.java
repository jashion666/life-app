package app.miniprogram.application.express.controller;

import app.miniprogram.application.express.service.ExpressService;
import app.miniprogram.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Autowired
    private ExpressService expressService;

    @RequestMapping("list")
    public ResponseEntity<JsonResult> getExpressList() {
        return new ResponseEntity<>(JsonResult.success("第一次调用成功！！！"), HttpStatus.OK);
    }

    @RequestMapping("query")
    public ResponseEntity<JsonResult> queryExpress(@RequestParam("postId") String postId) {

        return new ResponseEntity<>(
                JsonResult.success(
                        expressService.queryExpressByGateWay(postId)), HttpStatus.OK);
    }
}
