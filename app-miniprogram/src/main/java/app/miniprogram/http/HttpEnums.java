package app.miniprogram.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author :wkh.
 * @date :2019/5/17.
 */
@AllArgsConstructor
@Getter
public enum HttpEnums {

    // TODO 其他的code 一一对应
    /**
     * 服务器内部错误.
     */
    INTERNAL_ERROR(500, "服务器内部错误"),
    /**
     * 服务器内部错误.
     */
    PAGE_NOT_FOUND(404, "页面未找到");

    private Integer code;
    private String value;

}
