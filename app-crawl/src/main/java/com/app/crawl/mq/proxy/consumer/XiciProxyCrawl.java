package com.app.crawl.mq.proxy.consumer;

import com.app.utils.http.HttpUtils;
import com.app.utils.http.ProxyInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * 从西刺抓取
 *
 * @author :wkh.
 * @date :2019/8/9.
 */
public class XiciProxyCrawl extends AbstractProxy {

    private static final int MAX_PAGE = 10;

    private static final String[] URLS =
            {
                    "http://www.xicidaili.com/nn/",
                    "http://www.xicidaili.com/nt/",
                    "http://www.xicidaili.com/wt/",
                    "http://www.kuaidaili.com/free/inha/",
                    "http://www.kuaidaili.com/free/intr/"
            };


    public XiciProxyCrawl(Integer wantedNumber, List<ProxyInfo> excludeProxyList) {
        super(wantedNumber, excludeProxyList);
    }

    @Override
    public List<ProxyInfo> startCrawler() {
        for (String url : URLS) {
            for (int j = 1; j <= MAX_PAGE; j++) {
                if (size() >= wantedNumber) {
                    return proxyList;
                }
                getProxyIp(url, j);
            }
        }
        return proxyList;
    }

    private void getProxyIp(String baseUrl, int page) {
        try {
            // 爬取html
            Document doc = getDocument(baseUrl + page + "/");
            // 解析并获得ip
            analysisDocument(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, sdch")
                .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                .header("Cache-Control", "max-age=0")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                .header("Cookie", "Hm_lvt_7ed65b1cc4b810e9fd37959c9bb51b31=1462812244; _gat=1; _ga=GA1.2.1061361785.1462812244")
                .header("Host", "www.kuaidaili.com")
                .header("Referer", "http://www.kuaidaili.com/free/outha/")
                .timeout(3 * 1000)
                .get();
    }

    private void analysisDocument(Document doc) {
        Elements tr = doc.getElementById("ip_list").getElementsByTag("tr");
        for (Element e : tr) {
            if (size() >= wantedNumber) {
                return;
            }
            Elements td = e.getElementsByTag("td");
            if (td.size() <= 0) {
                continue;
            }
            // 只获可用1天以上的ip
            if (!td.get(8).text().contains("天")) {
                continue;
            }
            String ip = td.get(1).text();
            String port = td.get(2).text();
            if (HttpUtils.checkProxy(ip, Integer.parseInt(port))) {
                System.out.println("从西刺代理获取到可用代理IP ==》 " + ip + " " + port);
                addProxy(ip, Integer.valueOf(port));
            }

        }
    }
}
