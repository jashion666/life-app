package com.app.client.test.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.app.client.redis.RedisClient;
import com.life.app.service.DemoService;
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
public class DemoController {

    @Autowired
    RedisClient redisClient;

    @Reference(version = "1.0.0", group = "DemoServiceImpl")
    private DemoService demoService;

    @Reference(version = "1.0.0", group = "CarServiceImpl")
    private DemoService carService;

    @RequestMapping("/test")
    public Map<String, Object> hello() {
        Map<String, Object> map = new HashMap<>(16);
        map.put(demoService.sayHello("WORLD"), this.carService.sayHello("66"));
        redisClient.set("a", map);
        return map;
    }
}
