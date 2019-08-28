package com.app.crawl;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

/**
 * 爬虫服务端（不启用数据库）
 *
 * @author :wkh.
 */
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class, DataSourceAutoConfiguration.class})
@PropertySource("classpath:/dubbo-provider.properties")
@EnableDubbo(scanBasePackages = "com.app.crawl")
public class AppCrawApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppCrawApplication.class, args);
    }

}
