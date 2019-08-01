package com.app.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个Stomp 协议的endpoint指定URL为mySocket,并用.withSockJS()指定 SockJS协议。.setAllowedOrigins("*")设置跨域
        registry.addEndpoint("/mySocket").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //配置消息代理(message broker)
        //将消息传回给以‘/topic’开头的客户端

        registry.enableSimpleBroker("/topic");

    }
}