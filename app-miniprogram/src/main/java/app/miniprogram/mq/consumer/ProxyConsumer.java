package app.miniprogram.mq.consumer;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.enums.HttpEnums;
import app.miniprogram.redis.RedisClient;
import app.miniprogram.utils.CommonUtil;
import com.app.utils.http.CloudProxyCrawl;
import com.app.utils.http.ProxyInfo;
import com.app.utils.http.XiciProxyCrawl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
@Component
@RabbitListener(queues = {"proxy.direct.queue.1"})
@Slf4j
public class ProxyConsumer {

    @Autowired
    private RedisClient redisClient;

    @RabbitHandler
    public void process(Integer num) {
        // TODO 列队同时处理
        List<ProxyInfo> proxyInfoList = CommonUtil.getProxyInfoList(redisClient);
        if (proxyInfoList.size() == Constants.MAX_IP_NUMBER) {
            return;
        }
        log.info("列队处理 :");
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

    }

}
