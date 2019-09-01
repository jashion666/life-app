package app.miniprogram.security.service.impl;

import app.miniprogram.application.login.entity.UserEntity;
import app.miniprogram.application.login.entity.UserToken;
import app.miniprogram.application.login.service.UserService;
import app.miniprogram.security.entity.Code2SessionEntity;
import app.miniprogram.security.jwt.JwtService;
import app.miniprogram.security.service.WxAccountService;
import app.miniprogram.utils.JsonUtil;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 对接小程序接口code2Session以及获取token接口
 *
 * @author :wkh.
 * @date :2019/8/30.
 */
@Service
public class WxAccountServiceImpl implements WxAccountService {

    @Value("${wx.app.id}")
    private String appid;

    @Value("${wx.app.secret}")
    private String appSecret;

    @Value("${wx.code2.session.url}")
    private String wxCode2SessionUrl;

    private static final String WX_CODE2_SESSION_SUCCESS_CODE = "0";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    /**
     * 获取登陆token
     *
     * @return 登录用token
     */
    @Override
    public UserToken getWxLoginToken(String code) throws IOException {

        Code2SessionEntity code2SessionEntity = getCode2Session(code);
        checkCode2Session(code2SessionEntity);

        UserEntity entity = userService.getByWxOpenid(code2SessionEntity.getOpenid());
        reset(entity, code2SessionEntity);

        // 开始生成token
        return new UserToken(entity.getUId(), jwtService.createTokenByWxAccount(entity));
    }

    private void reset(UserEntity entity, Code2SessionEntity code2SessionEntity) {
        // 从数据库查询次openid是否存在 ，不存在则创建新的
        if (entity == null) {
            entity = new UserEntity();
            entity.setOpenId(code2SessionEntity.getOpenid());
            entity.setSessionKey(code2SessionEntity.getSession_key());
            userService.save(entity);
        } else {
            entity.setSessionKey(code2SessionEntity.getSession_key());
            userService.update(entity);
        }
    }

    /**
     * 微信的 code2session 接口
     */
    private Code2SessionEntity getCode2Session(String code) throws IOException {
        Map<String, String> params = new HashMap<>(4);
        params.put("appid", appid);
        params.put("secret", appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        HttpClient httpClient = new HttpClientImpl();
        return analysisCode2Session(httpClient.get(wxCode2SessionUrl, params));
    }

    private Code2SessionEntity analysisCode2Session(String code2Session) throws IOException {
        return new JsonUtil().getCustomObjectMapper().readValue(code2Session, new TypeReference<Code2SessionEntity>() {
        });
    }

    private void checkCode2Session(Code2SessionEntity entity) {
        if (entity.getErrcode() == null) {
            return;
        }
        if (!WX_CODE2_SESSION_SUCCESS_CODE.equals(entity.getErrcode())) {
            throw new AuthenticationException("获取code2session失败");
        }
    }
}
