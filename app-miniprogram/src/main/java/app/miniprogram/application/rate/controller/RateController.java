package app.miniprogram.application.rate.controller;

import app.miniprogram.application.constant.RedisKeyConstants;
import app.miniprogram.utils.JsonResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.app.redis.RedisClient;
import com.app.service.craw.rate.RateCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :wkh.
 * @date :2019/10/21.
 */
@RestController
@RequestMapping("/rate")
public class RateController {

    @Autowired
    private RedisClient redisClient;

    @Reference(version = "1.0.0")
    private RateCrawlService rateCrawLService;

    @RequestMapping("/init")
    public ResponseEntity<JsonResult> init() {
        try {
            // redis内没有数据调用爬虫rpc开始爬取
            Object redisObj = redisClient.get(RedisKeyConstants.RATE);
            if (ObjectUtils.isEmpty(redisObj)) {
                redisObj = rateCrawLService.getRateList();
            }
            return new ResponseEntity<>(JsonResult.success(redisObj), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(JsonResult.failed("查询失败"), HttpStatus.OK);
        }
    }
}
