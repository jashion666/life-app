package app.miniprogram.scheduled;

import app.miniprogram.application.constant.RedisKeyConstants;
import app.miniprogram.socket.rate.WebSocketRate;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.app.model.rate.RateDto;
import com.app.redis.RedisClient;
import com.app.service.craw.rate.RateCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/10/1.
 */
@Component
@Slf4j
public class RateTask {

    @Reference(version = "1.0.0")
    private RateCrawlService rateCrawLService;

    @Autowired
    private RedisClient redisClient;

    /**
     * 每20分钟检测一次。
     */
    @Scheduled(cron = "0 0/20 * * * *")
    public void send() throws Exception {
        if (WebSocketRate.size() > 0) {
            log.debug("定时推送");
            List<RateDto> result = rateCrawLService.getRateList();
            redisClient.set(RedisKeyConstants.RATE, result);
            WebSocketRate.sendAll(JSON.toJSONString(rateCrawLService.getRateList()));
        }
    }
}
