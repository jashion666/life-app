package com.app.crawl.mq.consumer;

import com.app.constant.CommonConstant;
import com.app.enums.HttpEnums;
import com.app.redis.RedisClient;
import com.app.utils.http.HttpUtils;
import com.app.utils.http.ProxyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    @Autowired
    private ThreadCrawlTask threadCrawlTask;

    /**
     * 爬取ip处理，每次只允许一个请求通过，并用redis锁住20分钟，当这个请求结束之后在释放锁
     */
    @RabbitHandler
    public void process(List<ProxyInfo> proxyInfoList) throws ExecutionException, InterruptedException {

        // 利用redis锁住
        if (isLocked()) {
            return;
        }

        log.info("爬取ip任务开始 ");
        getRedisLock();

        removeInvalidIp(proxyInfoList);

        execute(getInvalidIpNumber(proxyInfoList), proxyInfoList);

        reset(proxyInfoList);

        releaseRedisLock();
        log.info("任务结束");
    }

    /**
     * 开始执行爬取
     *
     * @param invalidIpNumber 失效ip的数量（需要爬取的数量）
     * @param proxyInfoList   ip集合
     * @throws ExecutionException   e
     * @throws InterruptedException e
     */
    private void execute(int invalidIpNumber, List<ProxyInfo> proxyInfoList) throws ExecutionException, InterruptedException {

        if (invalidIpNumber == 0) {
            return;
        }

        // 因为废弃ip已经移除所以重新设置一下
        reset(proxyInfoList);

        int cloudTaskNumber = Double.valueOf(Math.floor((double) invalidIpNumber / 2)).intValue();
        int xiciTaskNumber = invalidIpNumber - cloudTaskNumber;

        Future<List<ProxyInfo>> xiciFuture = threadCrawlTask.xiciTask(xiciTaskNumber, proxyInfoList);
        Future<List<ProxyInfo>> cloudFuture = threadCrawlTask.cloudTask(cloudTaskNumber, proxyInfoList);

        for (; ; ) {
            if (cloudFuture.isDone() && xiciFuture.isDone()) {
                if (cloudFuture.get() != null) {
                    proxyInfoList.addAll(cloudFuture.get());
                }
                if (xiciFuture.get() != null) {
                    proxyInfoList.addAll(xiciFuture.get());
                }
                break;
            }
            // 每隔两秒检查一次
            Thread.sleep(2000);
        }
    }

    /**
     * 移除不可用ip
     */
    private void removeInvalidIp(List<ProxyInfo> proxyInfoList) {
        Iterator iterator = proxyInfoList.iterator();
        while (iterator.hasNext()) {
            ProxyInfo item = (ProxyInfo) iterator.next();
            if (!HttpUtils.checkProxy(item.getIp(), item.getPort())) {
                iterator.remove();
                log.info("代理：  " + item.toString() + "失效");
            }
        }
    }

    /**
     * 获取失效ip的数量
     */
    private int getInvalidIpNumber(List<ProxyInfo> proxyInfoList) {
        return CommonConstant.MAX_IP_NUMBER - proxyInfoList.size();
    }

    /**
     * 获取redis锁
     */
    private boolean isLocked() {
        return !StringUtils.isEmpty(redisClient.get(HttpEnums.PROXY_PROCESS_KEY.getValue()));
    }

    /**
     * 获取redis锁
     */
    private void getRedisLock() {
        redisClient.set(HttpEnums.PROXY_PROCESS_KEY.getValue(), "1", 1200L);
    }

    /**
     * 释放redis锁
     */
    private void releaseRedisLock() {
        redisClient.remove(HttpEnums.PROXY_PROCESS_KEY.getValue());
    }

    /**
     * 重新将ip存入redis
     */
    private void reset(List<ProxyInfo> proxyInfoList) {
        // 2小时
        redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList, 1200L);
    }

}
