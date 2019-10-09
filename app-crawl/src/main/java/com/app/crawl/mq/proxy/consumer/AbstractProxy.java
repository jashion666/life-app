package com.app.crawl.mq.proxy.consumer;

import com.app.utils.http.ProxyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
public abstract class AbstractProxy {

    /**
     * 默认ip数量为3
     */
    private static final Integer DEFAULT_NUMBER = 3;

    /**
     * 默认http.
     */
    private static final String SCHEME = "http";

    /**
     * 想要获得的ip数量
     */
    Integer wantedNumber;

    /**
     * ip集合
     */
    List<ProxyInfo> proxyList;

    /**
     * 调用方传过来，排除已经获得的ip
     */
    private List<ProxyInfo> excludeProxyList;

    AbstractProxy() {
        this(DEFAULT_NUMBER, null);
    }

    AbstractProxy(Integer wantedNumber) {
        this(wantedNumber, null);
    }

    AbstractProxy(Integer wantedNumber, List<ProxyInfo> excludeProxyList) {
        this.wantedNumber = wantedNumber;
        this.excludeProxyList = excludeProxyList;
        initProxyList();
    }

    /**
     * 获取代理
     *
     * @return list 代理集合
     */
    protected abstract List<ProxyInfo> startCrawler();

    private void initProxyList() {
        proxyList = new ArrayList<>();
        excludeProxyList = Optional.ofNullable(excludeProxyList).orElse(new ArrayList<>());
    }

    int size() {
        return proxyList.size();
    }

    void addProxy(String ip, Integer port) {
        ProxyInfo info = new ProxyInfo(ip, port, SCHEME);
        if (excludeProxyList.contains(info)) {
            return;
        }
        proxyList.add(info);
    }

}
