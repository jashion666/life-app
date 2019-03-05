package com.life.app.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/spring/dubbo-provider.properties")
@EnableDubbo(scanBasePackages = "com.life.app.server.service.impl")
public class AppServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AppServerApplication.class, args);
  }

}
