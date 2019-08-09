package com.app.utils.http;

import org.jsoup.Jsoup;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
public class HttpUtils {

    /**
     * 测试ip的地址
     */
    private static final String TEST_URL = "https://www.kuaidi100.com/";

    /**
     * 判断ip和端口是否有效
     *
     * @param ip   ip地址
     * @param port 端口号
     * @return boolean
     */
    public static boolean checkProxy(String ip, Integer port) {
        try {
            Jsoup.connect(TEST_URL).timeout(2 * 1000).proxy(ip, port).get();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
