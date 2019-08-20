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
     * 快递类型文本.
     */
    private String typeText;
    /**
     * 物流轨迹.
     */
    private String trajectory;
    /**
     * 快递最后一次更新年月.
     */
    private String ymd;
    /**
     * 快递最后一次更新年月.
     */
    private LocalDateTime lastUpdateTime;
    /**
     * 快递最后一次更新的物流.
     */
    private String value;
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