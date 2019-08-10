package app.miniprogram.enums;

import lombok.Getter;

/**
 * @author :wkh.
 * @date :2019/8/6.
 */
@Getter
public enum HttpEnums {
    /**
     * redis 中代理ip的key
     */
    PROXY_KEY("proxyKey"),

    /**
     * 正在获取ip的key
     */
    PROXY_PROCESS_KEY("proxyProcessKey");

    private String value;

    HttpEnums(String value) {
        this.value = value;
    }
}
