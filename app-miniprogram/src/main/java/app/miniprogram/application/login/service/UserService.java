package app.miniprogram.application.login.service;

import app.miniprogram.application.login.entity.UserEntity;

/**
 * @author :wkh.
 * @date :2019/8/30.
 */

public interface UserService {

    /**
     * 通过openid检索用户信息
     *
     * @param openId openId
     * @return 用户信息
     */
    UserEntity getByWxOpenid(String openId);

    /**
     * 检索用户信息并且带有权限
     *
     * @param openId openId
     * @return 用户信息
     */
    UserEntity getWithRoles(String openId);

    /**
     * 插入操作
     *
     * @param record openId
     * @return 用户信息
     */
    int save(UserEntity record);

    /**
     * 更新操作
     *
     * @param record openId
     * @return 用户信息
     */
    int update(UserEntity record);
}
