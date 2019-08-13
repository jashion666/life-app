package app.miniprogram.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
@Component
@Slf4j
public class ProxyProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Integer msg) {
        rabbitTemplate.setExchange("proxy.direct.exchange");
        rabbitTemplate.setRoutingKey("proxy.direct.routing.key.1");
        rabbitTemplate.convertAndSend(msg);
    }
}
