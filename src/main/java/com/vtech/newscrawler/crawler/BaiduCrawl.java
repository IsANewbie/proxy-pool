package com.vtech.newscrawler.crawler;

import com.alibaba.fastjson.JSON;
import com.vtech.newscrawler.entity.baidu.News;
import com.vtech.newscrawler.entity.baidu.Root;
import com.vtech.newscrawler.util.UicodeBackslashU;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BaiduCrawl extends BaseCrawl {
    static HttpGet request = new HttpGet();
    static Map<String,String> param = new HashMap<>();
    public void init() {
        //设置请求头
        request.setHeader("Accept","*/*");
        request.setHeader("User-Agent", "baiduyuedu/8.3.3 (iPhone; iOS 13.3.1; Scale/3.00)");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Accept-Language","zh-Hans-CN;q=1, zh-Hant-CN;q=0.9");
        request.setHeader("Accept-Encoding","gzip, deflate, br");
        request.setHeader("Host", "news.baidu.com");
        request.setHeader("Content-Type","application/json");
        request.setHeader("Cookie","BAIDUID=E762128FF974792B21A504620E884AD0:FG=1");

        //设置固定请求参数
        param.put("_cfrom","1099a");
        param.put("_cuid2","6DFACA12A288C56105E094F2002174BBD68D7B2A2OMTJGCKDCD");
        param.put("_from","1099a");
        param.put("_network","53_0");
        param.put("_ua","gavihgaMvC9Vrmqlk4D0toabs8UsLqqqB");
        param.put("_uid","Yu2KtYOq-8g1u2iJ0OHqi_a3StYNPvuc_O2f8gu9HugcuSiplavraqqHB");
        param.put("_ut","99mjqkyfs8zV9mYtfNvWioaGs8qlC");
        param.put("baiduid","825888965556c8a040d9986e485455cf51a7f56c");
        param.put("cen","uid_ua_ut");
        param.put("cuid","825888965556c8a040d9986e485455cf51a7f56c");
        param.put("mid","825888965556c8a040d9986e485455cf51a7f56c");
        param.put("os","iphone");
        param.put("pd","newsplus");
        param.put("rn","20");
        param.put("sv","8.3.3");
    }

    public List<News> getNews(String keywords){
        List<News> news = new ArrayList<>();
        int pn =0;
        init();
        param.put("s",keywords);
        boolean flag = true;
        while (flag){
            param.put("pn",pn+"");
            String urlStr = "https://news.baidu.com/sn/api/search";
            // 创建uri
            URIBuilder builder = null;
            URI uri = null;
            try {
                builder = new URIBuilder(urlStr);
                for (String key: param.keySet()){
                    builder.addParameter(key,param.get(key));
                }
                uri = builder.build();
                request.setURI(uri);
                String html = getRespJSONByRequest(request);
                html = UicodeBackslashU.unicodeToCn(html);
                Root root = JSON.parseObject(html,Root.class);
                flag = root.getData().isHasmore();
                List<News> sourceData = root.getData().getNews();
                List<News> newData = sourceData.stream()
                                                .filter((News newObj) -> newObj.getTitle().contains(keywords))
                                                .collect(Collectors.toList());
                if(newData.size() == 0){
                    flag = false;
                }
                news.addAll(newData);
                pn+=10;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        }
//        List<News> result = null;
//        result = news.stream()
//                .filter((News s) -> s.getTitle().contains(keywords))
//                .collect(Collectors.toList());
        return news;
    }


    public static void main(String[] args) {
        BaiduCrawl baiduCrawl = new BaiduCrawl();
        List<News> news = baiduCrawl.getNews("基金");
        System.out.println(news.size());
//        news.forEach(o -> {
//            System.out.println(o.toString());
//
//        });
    }
}
