package com.vtech.newscrawler.crawler;/**
 * @author zoubin
 * @create 2020/5/18 - 23:15
 */

import com.vtech.newscrawler.entity.excel.ExcelData;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName SinaCrawl
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/18 23:15
 **/
@Component
public class SinaCrawl extends BaseCrawl {
    static HttpGet request = new HttpGet();
    static Map<String,String> param = new HashMap<>();
    public static void init() {
        //设置请求头
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Accept-Language","zh-Hans-CN;q=1, zh-Hant-CN;q=0.9");
        request.setHeader("Accept-encoding","gzip");
        request.setHeader("Content-Type","text/html;charset=utf-8");

        //设置固定请求参数
        param.put("c","news");
        param.put("range","title");
        param.put("num","10");
    }

    public List<ExcelData> getSinaNews(String keywords){
        List<ExcelData> news = new ArrayList<>();
        int pn =0;
        init();
        param.put("q",keywords);
        boolean flag = true;
        while (flag){
            param.put("page",pn+"");
            String urlStr = "https://search.sina.com.cn/";
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
                List<ExcelData> tempList = parseHtml(html,keywords);
                if(CollectionUtils.isEmpty(tempList)){
                    flag = false;
                }
                news.addAll(tempList);
                pn+=1;
                System.out.println(pn);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        }
        return news;
    }

    private static List<ExcelData> parseHtml(String html, String keyWord){
        List<ExcelData> newsResult = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element result_span = doc.select(".result").first();
        Elements result_list = result_span.select(".box-result");
        for (Element result : result_list) {
            ExcelData excelData = new ExcelData();
            Element info = result.select("h2").first();
            String title = info.select("a").first().text();
            if(title.contains(keyWord)){
                String url = info.select("a").first().attr("href");
                String mediaAndDate = info.select(".fgray_time").text();
                String media = "未知媒体";
                String date = "未知发布时间";
                if(StringUtils.isNotEmpty(mediaAndDate)){
                    media = mediaAndDate.split(" ")[0];
                    date = mediaAndDate.split(" ")[1];
                }
                excelData.setKeyword(keyWord);
                excelData.setChannel("新浪财经");
                excelData.setMedia(media);
                excelData.setTitle(title);
                excelData.setUrl(url);
                excelData.setDate(date);
                newsResult.add(excelData);
            }
        }
        return newsResult;
    }

//    public static void main(String[] args) {
//        getBaiduNews("贵州茅台").forEach(o ->{
//            System.out.println(o.toString());
//        });
//    }
}
