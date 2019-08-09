package com.app.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author :wkh.
 * @date :2019/8/5.
 */
@Slf4j
public class ProxyCrawl implements Runnable {

    public static void main(String[] args) {
        CloudProxyCrawl cloudProxyCrawl = new CloudProxyCrawl(1);
        List<ProxyInfo> list = cloudProxyCrawl.startCrawler();
        list.forEach(System.out::println);
    }


    @Override
    public void run() {

    }


}

