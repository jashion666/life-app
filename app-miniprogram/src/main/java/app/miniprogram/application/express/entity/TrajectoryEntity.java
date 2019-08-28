package app.miniprogram.application.express.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/8/13.
 */
@NoArgsConstructor
@Data
public class TrajectoryEntity {

    private String com;
    private String ischeck;
    private String condition;
    private String nu;
    private String state;
    private String message;
    private String status;
    private List<DataBean> data;

    @NoArgsConstructor
    @Data
    public static class DataBean {

        private String ftime;
        private String context;
        private String location;
        private String time;
    }
}
