package com.app.client.test.service.impl;

import com.app.client.redis.RedisClient;
import com.app.client.test.service.IDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author :wkh
 */
@Service
@Slf4j
public class DemoServiceImpl implements IDemoService {

    @Autowired
    RedisClient redisClient;

    @Override
    public int insert() {
        return 0;
    }
}
