package app.miniprogram.scheduled;

import app.miniprogram.enums.HttpEnums;
import app.miniprogram.redis.RedisClient;
import app.miniprogram.utils.CommonUtil;
import com.app.utils.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :wkh.
 * @date :2019/8/6.
 */
@Component
@Slf4j
public class ProxyCrawTask {

    /**
     * 爬取1个可用ip
     */
    private static final Integer MAX_NUMBER = 1;

    private final RedisClient redisClient;

    /**
     * 每50分钟检测一次代理池ip存活情况，不足则补满。
     */
    @Scheduled(cron = "0 0/50 * * * *")
    public void crawl() {
        log.info("定时任务： 检测代理ip池开始 ");
        // redis中代理ip的集合
        List<ProxyInfo> proxyInfoList = CommonUtil.getProxyInfoList(redisClient);

        // 排除死亡的ip
        proxyInfoList = proxyInfoList
                .stream()
                .filter(proxyInfo -> HttpUtils.checkProxy(proxyInfo.getIp(), proxyInfo.getPort()))
                .collect(Collectors.toList());
        redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList);

        int diff = MAX_NUMBER - proxyInfoList.size();
        log.info("存活ip数量" + proxyInfoList.size());
        // 先从云代理获取
        if (diff > 0) {
            log.info("开始从云代理获取代理ip 数量为" + diff);
            CloudProxyCrawl proxyCrawl = new CloudProxyCrawl(MAX_NUMBER, proxyInfoList);
            proxyInfoList = proxyCrawl.startCrawler();
        }
        // 数量不够从西刺再获取
        diff = MAX_NUMBER - proxyInfoList.size();
        if (diff > 0) {
            log.info("开始从西刺理获取代理ip 数量为" + diff);
            XiciProxyCrawl proxyCrawl = new XiciProxyCrawl(MAX_NUMBER, proxyInfoList);
            proxyInfoList = proxyCrawl.startCrawler();
        }
        redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList);
        log.info("检测结束");
    }

    @Autowired
    public ProxyCrawTask(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

}
