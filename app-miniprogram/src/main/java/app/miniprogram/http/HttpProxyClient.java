package app.miniprogram.http;

import app.miniprogram.utils.CommonUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.app.enums.HttpEnums;
import com.app.redis.RedisClient;
import com.app.service.craw.express.ProxyCrawlService;
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

    @Reference(version = "1.0.0")
    private ProxyCrawlService proxyCrawlService;

    /**
     * 设置代理
     *
     * @return HttpClient
     */
    public HttpClientExtension getHttpProxy() {
        log.info("====>开始获取代理");

        List<ProxyInfo> proxyInfoList = CommonUtil.getProxyInfoList(redisClient);
        ProxyInfo info;
        try {
            info = getAvailableProxy(proxyInfoList);
        } catch (Exception e) {
            log.info("<====获取失败 " + e.getMessage());
            proxyCrawlService.crawProxy(proxyInfoList);
            return new HttpClientExtensionImpl();
        }
        log.info("<==== 代理获取成功 信息为：" + info.toString());
        return new HttpClientExtensionImpl(info);
    }

    private ProxyInfo getAvailableProxy(List<ProxyInfo> proxyInfoList) throws Exception {
        if (proxyInfoList.size() == 0) {
            throw new Exception("暂无可用ip，通知远程服务器获取新的ip");
        }

        Random random = new Random();
        ProxyInfo info = proxyInfoList.get(random.nextInt(proxyInfoList.size()));
        if (!HttpUtils.checkProxy(info.getIp(), info.getPort())) {
            throw new Exception("代理ip失效，通知远程服务器获取新的ip");
        }
        return info;
    }

    /**
     * 移除指定代理信息
     */
    public void removeTargetProxy(ProxyInfo proxyInfo) {
        if (proxyInfo == null) {
            return;
        }
        log.info("ip代理失效，移除此代理 信息为" + proxyInfo.toString());
        List<ProxyInfo> proxyInfoList = CommonUtil.getProxyInfoList(redisClient);
        proxyInfoList.remove(proxyInfo);
        redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList);
    }

}
