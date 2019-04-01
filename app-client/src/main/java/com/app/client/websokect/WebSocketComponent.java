package com.app.client.websokect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketComponent {

    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations;

    @Scheduled(fixedRate = 1000 * 60)
    public void sendMessage() {
        log.info("推送ing...");
        //将消息推送给‘、topic/ip’的客户端
        simpMessageSendingOperations.convertAndSend("/topic", "我是从服务器来的消息!");
    }
}
