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
     * 快递类型.
     */
    private String type;
    /**
     * 物流轨迹.
     */
    private String trajectory;
    /**
     * 完成标识.
     */
    private String completeFlag;
    /**
     * 物流轨迹.
     */
    private TrajectoryEntity trajectoryInfo;
    private LocalDateTime insertTime;
    private Integer insertId;
    private LocalDateTime updateTime;
    private Integer updateId;

    public ExpressEntity() {
    }

    public ExpressEntity(ExpressEntityKey expressEntityKey) {
        super(expressEntityKey.getUId(), expressEntityKey.getPostId());
    }

    public ExpressEntity(Integer uId, String postId) {
        super(uId, postId);
    }

}