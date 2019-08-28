package com.app.service.craw.express;

/**
 * @author :wkh.
 * @date :2019/8/28.
 */
public interface ProxyCrawlService {

    String test();

    /**
     * 爬取ip接口
     *
     * @param num 爬取ip数量
     */
    void crawProxy(Integer num);
}
