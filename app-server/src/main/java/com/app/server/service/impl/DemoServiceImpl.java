package com.app.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.life.app.service.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wkh.
 */
@Slf4j
@Service(version = "1.0.0", group = "DemoServiceImpl")
@org.springframework.stereotype.Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        log.info("提供者调用 入参================::{name}->" + name);
        return "Hello " + name;
    }
}
