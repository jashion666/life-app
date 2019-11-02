package app.miniprogram.scheduled;

import app.miniprogram.socket.rate.WebSocketRate;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.app.service.craw.rate.RateCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author :wkh.
 * @date :2019/10/1.
 */
@Component
@Slf4j
public class RateTask {

    @Reference(version = "1.0.0")
    private RateCrawlService rateCrawLService;

    /**
     * 每20分钟检测一次。
     */
    @Scheduled(cron = "0 0/50 * * * *")
    public void send() throws Exception {
        log.debug("定时推送税率信息");
        WebSocketRate.sendAll(JSON.toJSONString(rateCrawLService.getRateList()));
    }
}
