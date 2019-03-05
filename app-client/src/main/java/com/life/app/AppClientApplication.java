package com.life.app;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDubbo(scanBasePackages = "com.life.app.client")
@PropertySource("classpath:/spring/dubbo-consumer.properties")
public class AppClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(AppClientApplication.class, args);
  }

}
