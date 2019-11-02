package com.app;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author :wkh.
 */
@SpringBootApplication
public class AppAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppAdminApplication.class, args);
    }

    @RequestMapping("/")
    public String index()  {
        return "index.html";
    }
}
