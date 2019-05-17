package com.app.server.test.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.app.server.test.mapper.TestMapper;
import com.app.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wkh.
 */
@Slf4j
@Service(version = "1.0.0", group = "CarServiceImpl")
@org.springframework.stereotype.Service
public class CarServiceImpl implements DemoService {

    private final TestMapper testMapper;

    @Autowired
    public CarServiceImpl(TestMapper testMapper) {
        this.testMapper = testMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = "primaryTransactionManager")
    public String sayHello(String name) {
        log.info("入参================::{name}->" + name);
//        testMapper.delete(name);
        return "查询结果:" + testMapper.selectByName(name);
    }
}
