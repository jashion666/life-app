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

    /**
     * com : shentong
     * ischeck : 0
     * condition : 00
     * data : [{"ftime":"2019-07-31 10:05:05","context":"辽宁沈阳转运中心-已发往-辽宁东港市公司","location":"","time":"2019-07-31 10:05:05"},{"ftime":"2019-07-31 09:46:32","context":"已到达-辽宁沈阳转运中心","location":"","time":"2019-07-31 09:46:32"},{"ftime":"2019-07-30 02:35:53","context":"浙江杭州航空部-已装袋发往-辽宁沈阳转运中心","location":"","time":"2019-07-30 02:35:53"},{"ftime":"2019-07-30 02:35:53","context":"浙江杭州航空部-已进行装车扫描","location":"","time":"2019-07-30 02:35:53"},{"ftime":"2019-07-30 02:21:26","context":"已到达-浙江杭州航空部","location":"","time":"2019-07-30 02:21:26"},{"ftime":"2019-07-29 23:26:27","context":"浙江杭州萧山分拨中心-已装袋发往-浙江杭州航空部","location":"","time":"2019-07-29 23:26:27"},{"ftime":"2019-07-29 23:26:27","context":"浙江杭州萧山分拨中心-已进行装车扫描","location":"","time":"2019-07-29 23:26:27"},{"ftime":"2019-07-29 23:18:49","context":"浙江杭州萧山分拨中心-已发往-浙江杭州航空部","location":"","time":"2019-07-29 23:18:49"},{"ftime":"2019-07-29 23:18:49","context":"浙江杭州萧山分拨中心-已进行装袋扫描","location":"","time":"2019-07-29 23:18:49"},{"ftime":"2019-07-29 23:17:26","context":"已到达-浙江杭州萧山分拨中心","location":"","time":"2019-07-29 23:17:26"},{"ftime":"2019-07-29 23:17:25","context":"浙江杭州萧山分拨中心-出港自动化-已收件","location":"","time":"2019-07-29 23:17:25"},{"ftime":"2019-07-29 16:04:46","context":"浙江杭州滨江公司-马湖N(18667029772,0571-28081888)-已收件","location":"","time":"2019-07-29 16:04:46"}]
     * typeList : [{"noPre":"371890","noCount":2755,"startTime":"","id":"","comCode":"shentong"},{"noPre":"371890","noCount":17,"startTime":"","id":"","comCode":"yunda"}]
     * nu : 3718907935117
     * state : 0
     * message : ok
     * status : 200
     */

    private String com;
    private String ischeck;
    private String condition;
    private String nu;
    private String state;
    private String message;
    private String status;
    private List<DataBean> data;
    private List<ExpressTypeEntity> typeList;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        /**
         * ftime : 2019-07-31 10:05:05
         * context : 辽宁沈阳转运中心-已发往-辽宁东港市公司
         * location :
         * time : 2019-07-31 10:05:05
         */

        private String ftime;
        private String context;
        private String location;
        private String time;
    }
}
