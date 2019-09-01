package app.miniprogram.security.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 微信小程序 Code2Session 接口返回值实体类
 *
 * @author :wkh.
 * @date :2019/8/30.
 */
@Data
@ToString
public class Code2SessionEntity {

    /**
     * 用户唯一标识.
     */
    private String openid;
    /**
     * 会话密钥
     */
    private String session_key;
    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回 详情请见 https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html
     */
    private String unionid;
    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;
}
