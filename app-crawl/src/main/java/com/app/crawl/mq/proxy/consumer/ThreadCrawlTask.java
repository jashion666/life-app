package com.app.crawl.mq.proxy.consumer;

import com.app.utils.http.ProxyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author :wkh.
 * @date :2019/8/28.
 */
@Component
@Slf4j
public class ThreadCrawlTask {

    /**
     * 云代理爬取
     *
     * @return Future
     */
    @Async("asyncExecutor")
    public Future<List<ProxyInfo>> cloudTask(Integer wantNumber, List<ProxyInfo> excludeProxyList) {
        if (wantNumber == 0) {
            return new AsyncResult<>(null);
        }
        log.info("开启云代理爬取线程 爬取数量" + wantNumber);
        CloudProxyCrawl proxyCrawl = new CloudProxyCrawl(wantNumber, excludeProxyList);
        return new AsyncResult<>(proxyCrawl.startCrawler());
    }

    /**
     * 西刺代理爬取
     *
     * @return Future
     */
    @Async("asyncExecutor")
    public Future<List<ProxyInfo>> xiciTask(Integer wantNumber, List<ProxyInfo> excludeProxyList) {
        if (wantNumber == 0) {
            return new AsyncResult<>(null);
        }
        log.info("开启西刺代理爬取线程 爬取数量" + wantNumber);
        XiciProxyCrawl xiciProxyCrawl = new XiciProxyCrawl(wantNumber, excludeProxyList);
        return new AsyncResult<>(xiciProxyCrawl.startCrawler());
    }
}
