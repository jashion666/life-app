package com.app.service.craw.rate;

import com.app.model.rate.RateDto;

import java.util.List;

/**
 * @author :wkh.
 * @date :2019/10/1.
 */
public interface RateCrawlService {

    /**
     * 爬取税率接口
     */
    List<RateDto> getRateList() throws Exception;
}
