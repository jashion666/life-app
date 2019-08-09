package app.miniprogram.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
@Configuration
public class ProxyDirectConfig {

    @Bean
    public Queue directQueue1() {
        return new Queue("proxy.direct.queue.1");
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("proxy.direct.exchange");
    }

    @Bean
    public Binding bindingQueue1(){
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("proxy.direct.routing.key.1");
    }

}
