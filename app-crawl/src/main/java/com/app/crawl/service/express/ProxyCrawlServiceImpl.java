package com.app.crawl.service.express;

import com.alibaba.dubbo.config.annotation.Service;
import com.app.crawl.mq.producer.ProxyProducer;
import com.app.service.craw.express.ProxyCrawlService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author :wkh.
 * @date :2019/8/28.
 */
@Service(version = "1.0.0")
@org.springframework.stereotype.Service
public class ProxyCrawlServiceImpl implements ProxyCrawlService {

    @Autowired
    private ProxyProducer proxyProducer;

    @Override
    public String test() {
        return "hello world";
    }

    @Override
    public void crawProxy(Integer num) {
        // 异步通知rabbitmq爬取ip
        proxyProducer.send(num);
    }
}
