package com.app.crawl.mq.consumer;

import com.app.constant.CommonConstant;
import com.app.enums.HttpEnums;
import com.app.redis.RedisClient;
import com.app.utils.ApiUtil;
import com.app.utils.http.CloudProxyCrawl;
import com.app.utils.http.ProxyInfo;
import com.app.utils.http.XiciProxyCrawl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
@Component
@RabbitListener(queues = {"proxy.direct.crawl.queue.1"})
@Slf4j
public class ProxyConsumer {

    @Autowired
    private RedisClient redisClient;

    @RabbitHandler
    public void process(Integer num) {

        // 利用redis锁住
        if (!StringUtils.isEmpty(redisClient.get(HttpEnums.PROXY_PROCESS_KEY.getValue()))) {
            return;
        }

        List<ProxyInfo> proxyInfoList = ApiUtil.getProxyInfoList(redisClient);
        if (proxyInfoList.size() == CommonConstant.MAX_IP_NUMBER) {
            return;
        }

        // 防止多线程同时操作（只锁住20分钟）
        redisClient.set(HttpEnums.PROXY_PROCESS_KEY.getValue(), "1", 1200L);

        log.info("爬取ip任务开始 ");
        log.info("  从云代理获取代理ip 数量为" + num);
        CloudProxyCrawl proxyCrawl = new CloudProxyCrawl(num, proxyInfoList);
        List<ProxyInfo> crawlerList = proxyCrawl.startCrawler();

        proxyInfoList.addAll(crawlerList);

        // 数量不够从西刺再获取
        int diff = num - crawlerList.size();
        if (diff > 0) {
            log.info("  从西刺理获取代理ip 数量为" + diff);
            XiciProxyCrawl xiciProxyCrawl = new XiciProxyCrawl(diff, proxyInfoList);
            proxyInfoList = xiciProxyCrawl.startCrawler();
        }

        redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList);
        // 一个线程处理完成之后再释放锁
        redisClient.remove(HttpEnums.PROXY_PROCESS_KEY.getValue());
        log.info("任务结束");
    }


}
