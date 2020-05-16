package com.vtech.newscrawler.crawler;

import com.vtech.newscrawler.entity.excel.ExcelData;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class WechatCrawl extends BaseCrawl{
    static HttpGet request = new HttpGet();
    static Map<String,String> param = new HashMap<>();
    public static void init() {
        //设置请求头
        request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.setHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        request.setHeader("Accept-Encoding","gzip, deflate, br");
        request.setHeader("Host", "weixin.sogou.com");
        request.setHeader("Content-Type","application/json");
        request.setHeader("Cookie","ABTEST=4|1589334881|v1; SNUID=F57A772F989C3DE444907DAC9990659E; IPLOC=CN4403; SUID=6DE3EEB72423910A000000005EBB5361; SUID=6DE3EEB74F18910A000000005EBB5362; SUV=000A177EB7EEE36D5EBB5362E8358573; weixinIndexVisited=1; sct=4; UM_distinctid=17212706dc45e6-0197bd46c5d20a-39624307-144000-17212706dc6958; JSESSIONID=aaa8AAa4jaPB4c1ne4Rgx; Hm_lvt_751036519e4a758fea7041111c48b794=1589447258,1589447603; PHPSESSID=8lba80mdpnbehtn6of4jg9a746; CNZZDATA1272960299=640636450-1589443183-%7C1589448583; Hm_lpvt_751036519e4a758fea7041111c48b794=1589452105");

        //设置固定请求参数

        param.put("type","2");
        param.put("ie","utf8");
        param.put("_sug_","y");
        param.put("s_from","input");
        param.put("_sug_type_","");

    }

    public static String getsss(String keywords){
        int pn =1;
        String html = "";
                init();
        param.put("query",keywords);
        boolean flag = true;
        while (flag){
            param.put("page",pn+"");
            String urlStr = "https://weixin.sogou.com";
            // 创建uri
            URIBuilder builder = null;
            URI uri = null;
            try {
                builder = new URIBuilder(urlStr);
//                for (String key: param.keySet()){
//                    builder.addParameter(key,param.get(key));
//                }
                uri = builder.build();
                HttpHost proxy = new HttpHost("163.125.69.29",8650);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setProxy(proxy)
                        .setConnectTimeout(10000)
                        .setSocketTimeout(10000)
                        .setConnectionRequestTimeout(3000)
                        .build();
                request.setConfig(requestConfig);
                request.setURI(uri);
                System.out.println(uri.toString());
                html = getRespJSONByRequest(request);
                System.out.println(html);
                pn++;
                if(!continueCrawl(html,keywords)){
                    flag = false;
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return html;
    }

    public static boolean continueCrawl(String html, String keyWord) {
        Document doc = Jsoup.parse(html);
        boolean canContinue = true;
        Elements newsParent = doc.getElementsByClass("news-list");
        Elements news = newsParent.first().getElementsByTag("li");
        for (Element element : news) {
            Element textBox = element.getElementsByClass("news-list").first();
            Element TitleAndUrl = textBox.getElementsByTag("h3").first();
            if (Objects.isNull(TitleAndUrl)) {
                TitleAndUrl = element.getElementsByTag("h4").first();
            }
            String title = TitleAndUrl.select("a").text();
            canContinue = title.contains(keyWord);
        }
        return canContinue;
    }
    public static List<ExcelData> getNewsByHtml(String html){
        List<ExcelData> excelDataList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements newsParent = doc.getElementsByClass("news-list");
        Elements news = newsParent.first().getElementsByTag("li");
        for (Element element:news){
            ExcelData excelData = new ExcelData();
            Element textBox = element.getElementsByClass("news-list").first();
            Element TitleAndUrl = textBox.getElementsByTag("h3").first();
            Element dateAndMedia = textBox.getElementsByTag("div").first();
            if(Objects.isNull(TitleAndUrl)){
                TitleAndUrl = element.getElementsByTag("h4").first();
            }
            String aHref = TitleAndUrl.select("a").attr("href");
            String title = TitleAndUrl.select("a").text();
            String media = dateAndMedia.select("a").text();
            String date = dateAndMedia.select("span").text();
            excelData.setUrl(aHref);
            excelData.setTitle(title);
            excelData.setMedia(media);
            excelData.setDate(date);
            System.out.println(aHref);
            System.out.println(title);
            System.out.println(media);
            System.out.println(date);
            excelDataList.add(excelData);
        }
        return excelDataList;
    }
    public static void main(String[] args) {
        getNewsByHtml(getsss("瑞金证券"));
    }
}
