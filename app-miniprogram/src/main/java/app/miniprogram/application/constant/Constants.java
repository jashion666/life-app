package app.miniprogram.application.constant;

import com.app.constant.CommonConstant;

/**
 * @author :wkh.
 * @date :2019/5/26.
 */
public class Constants extends CommonConstant {
    /**
     * http返回成功code
     */
    public static final String HTTP_OK = "200";

    /**
     * 快递返回成功表示
     */
    public static final String EXPRESS_CHECK_OK = "0";

    /**
     * 快递未完成flag
     */
    public static final String EXPRESS_NOT_COMPLETE_FLAG = "0";

    /**
     * 快递派送中flg
     */
    public static final String EXPRESS_SENDING_FLAG = "5";

    /**
     * 快递完成flag
     */
    public static final String EXPRESS_COMPLETE_FLAG = "3";

    /**
     * 查询历史一行最多的文字数.
     */
    public static final Integer EXPRESS_MAX_NUMBER = 20;
    /**
     * 省略符.
     */
    public static final String ELLIPSIS = "...";

    /**
     * png.
     */
    public static final String PNG = ".png";

    /**
     * 插入标识.
     */
    public static final String INSERT = "1";

    /**
     * 更新标识.
     */
    public static final String UPDATE = "2";

    /**
     * 顺丰.
     */
    public static final String SPECIAL_EXPRESS_TYPE_SHUNFENG = "SF";

    /**
     * 京东.
     */
    public static final String SPECIAL_EXPRESS_TYPE_JD = "JD";

}
