package app.miniprogram.application.login.service.impl;

import app.miniprogram.application.login.entity.UserEntity;
import app.miniprogram.application.login.mapper.UserEntityMapper;
import app.miniprogram.application.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author :wkh.
 * @date :2019/8/30.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Override
    public UserEntity getByWxOpenid(String openId) {
        return userEntityMapper.selectByOpenId(openId);
    }

    @Override
    public int save(UserEntity record) {
        return userEntityMapper.insert(record);
    }

    @Override
    public int update(UserEntity record) {
        return userEntityMapper.updateByOpenId(record);
    }
}
