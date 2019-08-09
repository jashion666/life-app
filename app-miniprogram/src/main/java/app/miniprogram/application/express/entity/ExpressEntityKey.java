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
     * 快递的key.
     */
    private String key;

    public ExpressEntityKey() {
    }

    public ExpressEntityKey(Integer uId, String postId) {
        this.postId = postId;
        this.uId = uId;
    }

    public String getKey() {
        return this.uId + this.postId;
    }

}