package app.miniprogram.application.express.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author :wkh
 */
@Data
@ToString
public class ExpressEntityKey {
    /**
     * 用户id.
     */
    private Integer uId;
    /**
     * 快递单号.
     */
    private String postId;
    /**
     * 快递类型.
     */
    private String type;
    /**
     * 快递的key.
     */
    private String key;

    public ExpressEntityKey() {
    }

    public ExpressEntityKey(Integer uId, String postId, String type) {
        this.postId = postId;
        this.uId = uId;
        this.type = type;
    }

    public String getKey() {
        return this.uId + this.type + this.postId;
    }

}