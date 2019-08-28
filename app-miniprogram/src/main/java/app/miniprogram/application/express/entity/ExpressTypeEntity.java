package app.miniprogram.application.express.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author :wkh.
 * @date :2019/8/13.
 */
@NoArgsConstructor
@Data
public class ExpressTypeEntity {

    private String noPre;
    private int noCount;
    private String startTime;
    private String id;
    private String comCode;
    private String name;
    private String imgUrl;
}
