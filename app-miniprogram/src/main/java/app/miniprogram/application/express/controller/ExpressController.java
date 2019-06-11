package app.miniprogram.application.express.controller;

import app.miniprogram.application.express.service.ExpressService;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 快递业务接口
 *
 * @author :wkh.
 * @date :2019/5/17.
 */
@RestController
@RequestMapping("express")
@Slf4j
public class ExpressController {

    @Autowired
    private ExpressService expressService;

    @RequestMapping("list")
    public ResponseEntity<JsonResult> getExpressList() {
        return new ResponseEntity<>(JsonResult.success("第一次调用成功！！！"), HttpStatus.OK);
    }

    @RequestMapping("query")
    public ResponseEntity<JsonResult> queryExpress(@RequestParam("postId") String postId,
                                                   @RequestParam(value = "type", required = false) String type) {

        try {

            return new ResponseEntity<>(
                    JsonResult.success(
                            expressService.queryExpressByGateWay(postId, type)), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(
                    JsonResult.failed("接口请求失败"), HttpStatus.OK);
        }
    }
}
