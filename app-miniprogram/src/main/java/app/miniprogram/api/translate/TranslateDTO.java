package app.miniprogram.api.translate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/9/16.
 */
@NoArgsConstructor
@Data
public class TranslateDTO {

    private String from;
    private String to;
    private List<TransResultBean> trans_result;
    private String error_code;
    private String error_msg;

    @NoArgsConstructor
    @Data
    public static class TransResultBean {

        private String src;
        private String dst;
    }
}
