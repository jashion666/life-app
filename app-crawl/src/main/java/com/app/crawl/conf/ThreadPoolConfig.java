package com.app.crawl.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author :wkh.
 * @date :2019/8/28.
 */
@Configuration
public class ThreadPoolConfig {

    @Bean("asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        threadPool.setCorePoolSize(2);
        // 设置最大线程数
        threadPool.setMaxPoolSize(4);
        // 线程池所使用的缓冲队列
        threadPool.setQueueCapacity(100);
        // 设置线程活跃时间（秒）
        threadPool.setKeepAliveSeconds(60);
        //配置线程池中的线程的名称前缀
        threadPool.setThreadNamePrefix("async-thread-pool-");
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }

}
