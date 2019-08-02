package app.miniprogram.application.express.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author :wkh
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExpressEntity extends ExpressEntityKey {

    /**
     * 物流轨迹.
     */
    private String trajectory;
    /**
     * 完成标识.
     */
    private Integer completeFlag;
    /**
     * 物流轨迹map.
     */
    private Map<String, Object> trajectoryMap;
    private LocalDateTime insertTime;
    private Integer insertId;
    private LocalDateTime updateTime;
    private Integer updateId;

    public ExpressEntity() {
    }

    public ExpressEntity(ExpressEntityKey expressEntityKey) {
        super(expressEntityKey.getUId(), expressEntityKey.getPostId(), expressEntityKey.getType());
    }

    public ExpressEntity(Integer uId, String postId, String type) {
        super(uId, postId, type);
    }

}