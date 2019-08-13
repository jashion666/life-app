package app.miniprogram.application.express.controller;

import app.miniprogram.application.express.entity.ExpressEntity;
import app.miniprogram.application.express.service.ExpressService;
import app.miniprogram.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExpressController {

    private final ExpressService expressService;

    /**
     * 快递查询
     *
     * @param postId 快递单号
     * @param type   快递类型（非必须）
     * @return 查询结果
     */
    @RequestMapping("query")
    public ResponseEntity<JsonResult> queryExpress(@RequestParam("uId") Integer uId,
                                                   @RequestParam("postId") String postId,
                                                   @RequestParam(value = "type", required = false) String type) {

        try {
            ExpressEntity entity = expressService.getExpressInfo(uId, postId, type);
            expressService.saveExpress(entity);
            // 清理不必要数据之后返回给前台
            clearUnnecessaryData(entity);
            return new ResponseEntity<>(JsonResult.success(entity), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(JsonResult.failed("接口请求失败"), HttpStatus.OK);
        }
    }

    private void clearUnnecessaryData(ExpressEntity entity) {
        entity.setUId(null);
        entity.setUpdateId(null);
        entity.setInsertId(null);
        entity.setKey(null);
        entity.setInsertTime(null);
        entity.setUpdateId(null);
    }

    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }
}
