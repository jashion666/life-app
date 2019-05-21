package com.app.utils.http;


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
    PAGE_NOT_FOUND(404, "页面未找到"),
    /**
     * 服务器内部错误.
     */
    FORBIDDEN(403, "禁止访问该链接");

    private Integer code;
    private String value;

    /**
     * 根据code获取去value
     *
     * @param code 错误代码
     * @return 错误信息
     */
    public static String getValue(Integer code) {
        for (HttpEnums httpEnums : HttpEnums.values()) {
            if (httpEnums.getCode().equals(code)) {
                return httpEnums.getValue();
            }
        }
        return null;
    }
}
