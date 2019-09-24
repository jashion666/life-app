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
     * 未完成的物流是否有更新.
     */
    private Boolean hasChange;
    /**
     * 快递图标地址.
     */
    private String imgUrl;
    /**
     * 完成标识.
     */
    private String completeFlag;
    /**
     * 快递图标地址.
     */
    private String operateFlag;
    private LocalDateTime insertTime;
    private Integer insertId;
    private LocalDateTime updateTime;
    private Integer updateId;

    /**
     * 物流轨迹实体类.
     */
    private TrajectoryEntity trajectoryInfo;

    public ExpressEntity() {
    }

    public ExpressEntity(ExpressEntityKey expressEntityKey) {
        super(expressEntityKey.getUId(), expressEntityKey.getPostId());
    }

    public ExpressEntity(Integer uId, String postId) {
        super(uId, postId);
    }

}