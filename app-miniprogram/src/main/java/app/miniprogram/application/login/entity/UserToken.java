package app.miniprogram.application.login.entity;

import lombok.Data;

/**
 * @author :wkh.
 * @date :2019/8/31.
 */
@Data
public class UserToken {
    private Long uId;
    private String token;

    public UserToken(Long uId, String token) {
        this.uId = uId;
        this.token = token;
    }

}
