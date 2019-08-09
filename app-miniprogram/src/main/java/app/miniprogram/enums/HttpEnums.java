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
    PROXY_KEY("proxyKey");

    private String value;

    HttpEnums(String value) {
        this.value = value;
    }
}
