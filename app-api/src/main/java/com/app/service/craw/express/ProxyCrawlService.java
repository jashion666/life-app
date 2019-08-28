package com.app.service.craw.express;

import com.app.utils.http.ProxyInfo;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/8/28.
 */
public interface ProxyCrawlService {

    String test();

    /**
     * 爬取ip接口
     *
     * @param proxyInfoList redis中ip的集合（该消息通知到rabbitmq，然后检测所有ip是否存活，在重新设置ip）
     */
    void crawProxy(List<ProxyInfo> proxyInfoList);
}
