package com.app.crawl.service.rate;

import com.alibaba.dubbo.config.annotation.Service;
import com.app.model.rate.RateDto;
import com.app.service.craw.rate.RateCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :wkh.
 * @date :2019/10/1.
 */
@Service(version = "1.0.0")
@org.springframework.stereotype.Service
@Slf4j
public class RateCrawlServiceImpl implements RateCrawlService {

    @Override
    public List<RateDto> getRateList() throws Exception {
        log.debug("读取税率");
        return doCrawl();
    }

    private List<RateDto> doCrawl() throws Exception {
        Document doc = getDocument("https://www.huilv.cc/renminyinhanghuilv.html");
        Elements tables = doc.getElementsByClass("data-table");
        assert tables != null;
        Element table = tables.get(0);
        // 获取全部tr
        Elements allTr = table.getElementsByTag("tr");
        // 排除第一个表头
        allTr.remove(0);
        return getResultList(allTr);
    }

    private List<RateDto> getResultList(Elements allTr) {
        List<RateDto> resultList = new ArrayList<>(7);
        for (Element tr : allTr) {
            Elements tds = tr.getElementsByTag("td");
            RateDto rateDto = new RateDto();
            List<String> columnList = getColumn(tds);
            rateDto.setCurrencyName(columnList.get(0));
            rateDto.setTradingUnit(columnList.get(1));
            rateDto.setBuyingRate(columnList.get(2));
            rateDto.setCashPurchasePrice(columnList.get(3));
            rateDto.setSpotSellingPrice(columnList.get(4));
            rateDto.setCashSellingPrice(columnList.get(5));
            rateDto.setUpdateTime(columnList.get(6));
            resultList.add(rateDto);
        }
        return resultList;
    }

    private List<String> getColumn(Elements tds) {
        List<String> list = new ArrayList<>(6);
        for (Element td : tds) {
            Elements a = td.getElementsByTag("a");
            list.add(a.size() == 0 ? td.html() : a.get(0).html());
        }
        assert list.size() == 7;
        return list;
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "zh,en-US;q=0.9,en;q=0.8,zh-CN;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .header("Cookie", "Hm_lvt_a79aa26e235f42e6eee8234513479b89=1569461805; Hm_lpvt_a79aa26e235f42e6eee8234513479b89=1569568961")
                .header("Host", "www.huilv.cc")
                .timeout(10 * 1000)
                .get();
    }

    private Connection getProxy(String url) {
        return Jsoup.connect(url);
    }
}
