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

import java.util.List;

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

    @RequestMapping("test")
    public ResponseEntity<JsonResult> test() {
        return new ResponseEntity<>(JsonResult.success("测试接口"), HttpStatus.OK);
    }

    /**
     * 快递历史查询
     *
     * @param uId 用户id
     */
    @RequestMapping("history")
    public ResponseEntity<JsonResult> getHistory(@RequestParam("uId") Integer uId) {
        log.info("参数 uId:" + uId);
        List<ExpressEntity> historyList;
        try {
            historyList = expressService.getHistoryList(uId);
            clearUnnecessaryData(historyList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(JsonResult.failed("查询失败"), HttpStatus.OK);
        }

        return new ResponseEntity<>(JsonResult.success(historyList), HttpStatus.OK);
    }

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
        log.info("参数 uId:" + uId + " postId:" + postId + " type:" + type);
        try {
            ExpressEntity entity = expressService.getExpressInfo(uId, postId.trim(), type);
            expressService.saveExpress(entity);
            // 清理不必要数据之后返回给前台
            clearUnnecessaryData(entity);
            return new ResponseEntity<>(JsonResult.success(entity), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(JsonResult.failed("查询失败,请指定或者切换快递公司之后在进行尝试。"), HttpStatus.OK);
        }
    }

    /**
     * 快递查询
     *
     * @param uId    用户id
     * @param postId 快递单号
     */
    @RequestMapping("delete")
    public ResponseEntity<JsonResult> delete(@RequestParam("uId") Integer uId,
                                             @RequestParam("postId") String postId) {
        log.info("参数： uId=>" + uId + " postId =>" + postId);
        try {
            expressService.delete(uId, postId);
            return new ResponseEntity<>(JsonResult.success("删除成功"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseEntity<>(JsonResult.failed("删除失败"), HttpStatus.OK);
        }
    }

    private void clearUnnecessaryData(List<ExpressEntity> list) {
        list.forEach(this::clearUnnecessaryData);
    }

    private void clearUnnecessaryData(ExpressEntity entity) {
        entity.setTrajectory(null);
        entity.setUpdateId(null);
        entity.setInsertId(null);
        entity.setInsertTime(null);
        entity.setUpdateId(null);
    }

    @Autowired
    public ExpressController(ExpressService expressService) {
        this.expressService = expressService;
    }
}
