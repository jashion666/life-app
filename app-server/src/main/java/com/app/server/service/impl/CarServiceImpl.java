package com.app.server.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.life.app.service.DemoService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wkh.
 */
@Slf4j
@Service(version = "1.0.0", group = "CarServiceImpl")
@org.springframework.stereotype.Service
public class CarServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        log.info("入参================::{name}->" + name);
        return "贾维斯说:" + name;
    }
}
