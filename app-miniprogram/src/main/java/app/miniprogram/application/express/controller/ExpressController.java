package app.miniprogram.application.express.controller;

import app.miniprogram.application.express.service.impl.ExpressServiceImpl;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ExpressServiceImpl expressService;

    /**
     * 快递查询
     *
     * @param postId 快递单号
     * @param type   快递类型（非必须）
     * @return 查询结果
     */
    @RequestMapping("query")
    public ResponseEntity<JsonResult> queryExpress(@RequestParam("postId") String postId,
                                                   @RequestParam(value = "type", required = false) String type) {

        try {
            Map<String, Object> resultMap = expressService.getExpressMap(postId, type);
            // TODO 查询结果插入数据库逻辑
            expressService.insertExpress(resultMap);
            return new ResponseEntity<>(JsonResult.success(resultMap), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(JsonResult.failed("接口请求失败"), HttpStatus.OK);
        }
    }

    @Autowired
    public ExpressController(ExpressServiceImpl expressService) {
        this.expressService = expressService;
    }
}
