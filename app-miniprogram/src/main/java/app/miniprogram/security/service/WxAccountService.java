package app.miniprogram.security.service;

import app.miniprogram.application.login.entity.UserToken;

import java.io.IOException;

/**
 * @author :wkh.
 * @date :2019/8/30.
 */
public interface WxAccountService {

    /**
     * 获取登陆token的接口
     *
     * @param code wx.request获得的code
     * @return token
     * @throws IOException e
     */
    UserToken getWxLoginToken(String code) throws IOException;
}
