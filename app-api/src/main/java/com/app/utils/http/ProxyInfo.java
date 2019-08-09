package com.app.utils.http;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author :wkh.
 * @date :2019/8/5.
 */
@Data
@ToString
public class ProxyInfo implements Serializable {
    private String scheme;
    private String ip;
    private Integer port;

    public ProxyInfo() {
    }

    public ProxyInfo(String ip, Integer port, String scheme) {
        this.ip = ip;
        this.scheme = scheme;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProxyInfo)) {
            return false;
        }
        ProxyInfo proxyInfo = (ProxyInfo) o;
        return Objects.equals(scheme, proxyInfo.scheme) &&
                Objects.equals(ip, proxyInfo.ip) &&
                Objects.equals(port, proxyInfo.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheme, ip, port);
    }
}
