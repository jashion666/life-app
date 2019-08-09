package app.miniprogram.http;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.enums.HttpEnums;
import app.miniprogram.mq.producer.ProxyProducer;
import app.miniprogram.redis.RedisClient;
import app.miniprogram.utils.CommonUtil;
import com.app.utils.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author :wkh.
 * @date :2019/8/6.
 */
@Component
@Slf4j
public class HttpProxyClient {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private ProxyProducer mqProxyProducer;

    /**
     * 设置代理
     *
     * @return HttpClient
     */
    public HttpClient getHttpProxy() {
        List<ProxyInfo> proxyInfoList = CommonUtil.getProxyInfoList(redisClient);
        if (proxyInfoList.size() == 0) {
            mqProxyProducer.send(Constants.MAX_IP_NUMBER);
            return new HttpClientExtensionImpl();
        }
        Random random = new Random();
        // 随机获取一个ip
        ProxyInfo info = proxyInfoList.get(random.nextInt(proxyInfoList.size()));
        // 如果该ip无效删除redis里的值.并且通知rabbitMQ重新获取指定数量的ip.
        if (!HttpUtils.checkProxy(info.getIp(), info.getPort())) {
            proxyInfoList.remove(info);
            redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList);
            mqProxyProducer.send(Constants.WANTED_IP_NUMBER);
            return new HttpClientExtensionImpl();
        }
        log.info("开启代理 代理信息为：" + info.toString());
        return new HttpClientExtensionImpl(info);
    }

}
