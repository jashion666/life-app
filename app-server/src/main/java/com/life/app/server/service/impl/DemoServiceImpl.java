package com.life.app.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.life.app.service.DemoService;

/**
 * @author wkh.
 */
@Service(version = "1.0.0", group = "DemoServiceImpl")
@org.springframework.stereotype.Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("提供者调用 -------------------------------------------------------------------------");
        return "Hello " + name;
    }
}
