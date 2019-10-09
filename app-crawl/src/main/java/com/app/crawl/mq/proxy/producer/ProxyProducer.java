package com.app.crawl.mq.proxy.producer;

import com.app.utils.http.ProxyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 爬取代理mq提供者
 * @author :wkh.
 * @date :2019/8/9.
 */
@Component
@Slf4j
public class ProxyProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(List<ProxyInfo> proxyInfoList) {
        rabbitTemplate.setExchange("proxy.direct.crawl.exchange");
        rabbitTemplate.setRoutingKey("proxy.direct.crawl.routing.key.1");
        rabbitTemplate.convertAndSend(proxyInfoList);
    }
}
