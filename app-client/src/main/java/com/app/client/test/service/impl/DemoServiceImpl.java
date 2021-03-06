package com.app.client.test.service.impl;

import com.app.redis.RedisClient;
import com.app.client.test.service.IDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author :wkh
 */
@Service
@Slf4j
public class DemoServiceImpl implements IDemoService {

    @Autowired
    RedisClient redisClient;

    @Override
    public Integer insert() {
        return 0;
    }
}
