package com.app.client.test.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.app.client.redis.RedisClient;
import com.app.client.test.service.IDemoService;
import com.life.app.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wkh.
 */
@RestController
@RequestMapping("/demo")
@Slf4j
public class DemoController {

    @Autowired
    RedisClient redisClient;

    @Reference(version = "1.0.0", group = "DemoServiceImpl")
    private DemoService demoService;

    @Reference(version = "1.0.0", group = "CarServiceImpl")
    private DemoService carService;

    @Autowired
    private IDemoService clientDemoService;

    @RequestMapping("/test")
    public Map<String, Object> hello() throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put(demoService.sayHello("WORLD"), this.carService.sayHello("66"));
        clientDemoService.insert();
        log.info(redisClient.get("test1").toString());
        return map;
    }
}
