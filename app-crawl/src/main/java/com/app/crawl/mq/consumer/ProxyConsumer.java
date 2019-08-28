package com.app.crawl.mq.consumer;

import com.app.constant.CommonConstant;
import com.app.enums.HttpEnums;
import com.app.redis.RedisClient;
import com.app.utils.ApiUtil;
import com.app.utils.http.CloudProxyCrawl;
import com.app.utils.http.HttpUtils;
import com.app.utils.http.ProxyInfo;
import com.app.utils.http.XiciProxyCrawl;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
        if (!StringUtils.isEmpty(redisClient.get(HttpEnums.PROXY_PROCESS_KEY.getValue()))) {
            return;
        }

        log.info("爬取ip任务开始 ");
        // 防止多线程同时操作（只锁住20分钟）
        redisClient.set(HttpEnums.PROXY_PROCESS_KEY.getValue(), "1", 1200L);
        int allNumber = getInvalidIpNumber(proxyInfoList);
        log.info("需要的ip数量为" + allNumber);

        int cloudTaskNumber = Double.valueOf(Math.floor((double) allNumber / 2)).intValue();
        int xiciTaskNumber = allNumber - cloudTaskNumber;
        Future<List<ProxyInfo>> cloudFuture = threadCrawlTask.cloudTask(cloudTaskNumber, proxyInfoList);
        Future<List<ProxyInfo>> xiciFuture = threadCrawlTask.xiciTask(xiciTaskNumber, proxyInfoList);

        for (; ; ) {
            if (cloudFuture.isDone() && xiciFuture.isDone()) {
                proxyInfoList.addAll(cloudFuture.get());
                proxyInfoList.addAll(xiciFuture.get());
                break;
            }
            // 每隔两秒检查一次
            Thread.sleep(2000);
        }
        redisClient.set(HttpEnums.PROXY_KEY.getValue(), proxyInfoList);
        // 一个线程处理完成之后再释放锁
        redisClient.remove(HttpEnums.PROXY_PROCESS_KEY.getValue());
        log.info("任务结束");
    }

    private int getInvalidIpNumber(List<ProxyInfo> proxyInfoList) {

        int diff = CommonConstant.MAX_IP_NUMBER - proxyInfoList.size();
        int num = 0;
        for (ProxyInfo item : proxyInfoList) {
            if (!HttpUtils.checkProxy(item.getIp(), item.getPort())) {
                log.info("代理：  " + item.toString() + "失效");
                num++;
            }
        }
        return num + diff;
    }


}
