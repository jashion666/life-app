package com.app.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/dubbo-provider.properties")
@EnableDubbo(scanBasePackages = "com.app.server.service.impl")
public class AppServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AppServerApplication.class, args);
  }

}
