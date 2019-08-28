package com.app.utils.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * 从云代理抓取
 *
 * @author :wkh.
 * @date :2019/8/9.
 */
public class CloudProxyCrawl extends AbstractProxy{

    private static final String URL = "http://www.ip3366.net/?stype=1&page=";

    private static final int MAX_PAGE = 10;

    public CloudProxyCrawl() {
        super();
    }

    public CloudProxyCrawl(Integer wantedNumber) {
        super(wantedNumber);
    }

    public CloudProxyCrawl(Integer wantedNumber, List<ProxyInfo> excludeProxyList) {
        super(wantedNumber, excludeProxyList);
    }

    @Override
    public List<ProxyInfo> startCrawler() {
        for (int i = 1; i <= MAX_PAGE; i++) {
            // TODO 重构
            if (size() >= wantedNumber) {
                return proxyList;
            }
            getProxyIp(i);
        }
        return proxyList;
    }

    private void getProxyIp(int page) {
        try {
            // 爬取html
            Document doc = getDocument(URL + page);
            // 解析并获得ip
            analysisDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, sdch")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .header("Cache-Control", "max-age=0")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .header("Cookie", "UM_distinctid=16c6f3b82d5a2f-0b6cff06c54f5f-c343162-1fa400-16c6f3b82d694d; Hm_lvt_c4dd741ab3585e047d56cf99ebbbe102=1565234267; ASPSESSIONIDSSTQRAAR=CMEONAOAPFFLNBDPCFBELEII")
                .header("Host", "www.ip3366.net")
                .header("Referer", "http://www.ip3366.net")
                .timeout(3 * 1000)
                .get();
    }

    private void analysisDocument(Document doc) {
        Elements tr = doc.getElementsByClass("table table-bordered table-striped").get(0).getElementsByTag("tr");
        for (Element e : tr) {
            // TODO 重构
            if (size() >= wantedNumber) {
                return;
            }

            Elements td = e.getElementsByTag("td");
            if (td.size() <= 0) {
                continue;
            }

            String ip = td.get(0).text();
            String port = td.get(1).text();
            if (HttpUtils.checkProxy(ip, Integer.parseInt(port))) {
                System.out.println("从云代理获取到可用代理IP ==》 " + ip + " " + port);
                addProxy(ip, Integer.valueOf(port));
            }
        }
    }

}
