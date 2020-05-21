package com.vtech.newscrawler.crawler;

import com.alibaba.fastjson.JSON;
import com.vtech.newscrawler.common.RedisKey;
import com.vtech.newscrawler.entity.baidu.News;
import com.vtech.newscrawler.entity.baidu.Root;
import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.service.IProxyIpRedisService;
import com.vtech.newscrawler.util.ExcelUtils;
import com.vtech.newscrawler.util.UicodeBackslashU;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BaiduCrawl extends BaseCrawl {
    static HttpGet request = new HttpGet();
    static Map<String,String> param = new HashMap<>();
    private static final String[] TITLE_REGX = {"-","|"};
    //rsv_pq=d7477ec000034e5a&rsv_t=105dA1arygiwn1kYd9SklBzRWfYTSTpQH%2Bvy6itxr4%2BStt%2FlpYMJ6gmzGes
    public static void init() {
        //设置请求头
//        request.setHeader("Accept","*/*");
//        request.setHeader("User-Agent", "baiduyuedu/8.3.3 (iPhone; iOS 13.3.1; Scale/3.00)");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        request.setHeader("Connection", "keep-alive");
        request.setHeader("Accept-Language","zh-Hans-CN;q=1, zh-Hant-CN;q=0.9");
        request.setHeader("Accept-Encoding","gzip, deflate, br");
       // request.setHeader("Host", "news.baidu.com");
        request.setHeader("Content-Type","text/html;charset=utf-8");
        request.setHeader("Cookie","BAIDUID=E762128FF974792B21A504620E884AD0:FG=1");

        //设置固定请求参数
//        param.put("_cfrom","1099a");
//        param.put("_cuid2","6DFACA12A288C56105E094F2002174BBD68D7B2A2OMTJGCKDCD");
//        param.put("_from","1099a");
//        param.put("_network","53_0");
//        param.put("_ua","gavihgaMvC9Vrmqlk4D0toabs8UsLqqqB");
//        param.put("_uid","Yu2KtYOq-8g1u2iJ0OHqi_a3StYNPvuc_O2f8gu9HugcuSiplavraqqHB");
//        param.put("_ut","99mjqkyfs8zV9mYtfNvWioaGs8qlC");
//        param.put("baiduid","825888965556c8a040d9986e485455cf51a7f56c");
//        param.put("cen","uid_ua_ut");
//        param.put("cuid","825888965556c8a040d9986e485455cf51a7f56c");
//        param.put("mid","825888965556c8a040d9986e485455cf51a7f56c");
//        param.put("os","iphone");
//        param.put("pd","newsplus");
//        param.put("rsv_pq","d7477ec000034e5a");

        param.put("ie", "utf-8");//关键字编码格式
        param.put("rsv_idx", "1");//
        param.put("f", "8");//用户自立搜索，透露表现用户直接点击“百度一下”按键
        param.put("rsv_bq", "1");
        param.put("tn", "baidu");

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
    public List<ExcelData> getBaiduNews(String keywords){
        List<ExcelData> news = new ArrayList<>();
        int pn =0;
        String UserAgent = "Mozilla/5.0(WindowsNT10.0;Win64;x64)AppleWebKit/537.36(KHTML,likeGecko)Chrome/" + 59 + Math.round(Math.random() * 10) + ".0.3497." + Math.round(Math.random() * 100) + "Safari/537.36";
        init();
        param.put("wd",keywords);
        param.put("oq", keywords);//上次索引关键字
        request.setHeader("User-Agent",UserAgent);
        boolean flag = true;
        int i = 0;
        while (i < 50){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            param.put("pn",pn+"");
            String urlStr = "https://www.baidu.com/s";
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
                pn+=10;
                System.out.println(pn);
                System.out.println(tempList.size());
                System.out.println(html.length());
                System.out.println(uri.toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            i++;
        }
        return news;
    }

    public static void main(String[] args) throws URISyntaxException {
        BaiduCrawl baiduCrawl = new BaiduCrawl();
        List<ExcelData> excelData = baiduCrawl.getBaiduNews("瑞金证券");
        ExcelUtils.createExcel();
        HashMap<String,List<ExcelData>> params = new HashMap<>();
        params.put("百度",excelData);
        ExcelUtils.insertData(params);

        excelData.forEach(o -> {
            System.out.println(o.toString());
        });
        System.out.println(excelData.size());
//        getHtml("http://gdgp.chinaxinge.com/oicnvev/");
    }

    private static List<ExcelData> parseHtml(String html, String keyWord){

        List<ExcelData> newsResult = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element content_left = doc.select("#content_left").first();
        Elements search_results = content_left.select(".result");
        for(Element result : search_results){
            Element body = result.select(".c-abstract").first();
            if(Objects.isNull(body)){

            }else {
                Element a = result.select("a").first();
                String title = a.text();
                Element timeSpan = body.select("span").first();
                    String time = "";
                    if(!Objects.isNull(timeSpan)){
                        time = timeSpan.text();
                    }
                    Element mediaEle = result.select(".nor-src-wrap").first();
                    String url = a.attr("href");
                    url = getRealUrlFromBaiduUrl(url);
                    String media = Objects.isNull(mediaEle) ? "" : mediaEle.text();
                    System.out.println(title);
                    if(StringUtils.isNotBlank(url) && !url.startsWith("m.")){
                    if(title.contains(keyWord) && !media.contains("百度")){
                        ExcelData excelData = new ExcelData();
                        String TempHtml = getHtml(url);
                        boolean canAdd = true;
                        if(!TempHtml.equals("404")){
                            canAdd = TempHtml.contains(keyWord) && !TempHtml.equals("404");
                            canAdd = canAdd && !title.contains("【");
                            Document document = Jsoup.parse(TempHtml);
                            Elements titleEle =  document.getElementsByTag("title");
                            title = titleEle.isEmpty() ? title : titleEle.first().text();
                        }
                        if(canAdd){
                            String finalTitle = title;
                            String spareMedia = media;
                            if(title.contains("|")){
                                finalTitle = title.split("\\|")[0];
                                spareMedia = title.split("\\|")[1];
                            }else if(title.contains("_")){
                                finalTitle = title.split("_")[0];
                                spareMedia = title.split("_")[1];
                            }else if(title.contains("-")){
                                finalTitle = title.split("-")[0];
                                spareMedia = title.split("-")[1];
                            }else if(title.contains("�")){
                                finalTitle = "";
                            }
                            canAdd = !spareMedia.contains("【") || !media.contains("【") ;
                            excelData.setChannel("百度");
                            excelData.setKeyword(keyWord);
                            excelData.setMedia(StringUtils.isBlank(media) ? spareMedia : media);
                            excelData.setTitle(finalTitle);
                            excelData.setUrl(url);
                            excelData.setDate(time);
                            if(canAdd){
                                newsResult.add(excelData);
                            }
                        }

                    }
                    }
            }
        }
        return newsResult;
    }
    public static String getTitle(Element element, String condTitle){
        String result = "";
        boolean canContinue = element.children().isEmpty();
        if(canContinue){
            String title = element.text().replaceAll("\\s*", "");
            if(title.contains(condTitle)){
                result = element.text();
            }else {
                element = element.nextElementSibling();
                result = getTitle(element, condTitle);
            }
        }else {
            result = getTitle(element.firstElementSibling(),condTitle);
        }
        return result;
    }
    public static String getRealUrlFromBaiduUrl(String url) {
        Connection.Response res = null;
        int itimeout = 60000;
        try {
            res = Jsoup.connect(url).timeout(itimeout).method(Connection.Method.GET).followRedirects(false).execute();
            return res.header("Location");
        } catch (ConnectException e){
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String getBaiduSearch() throws URISyntaxException {
        BaiduCrawl baiduCrawl = new BaiduCrawl();
        init();
        URI uri = new URI("https://www.baidu.com/s?word=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8&pn=10");
        request.setURI(uri);
        String html = BaseCrawl.getRespJSONByRequest(request);
        return html;
    }
    private static String getHtml(String url){
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");

        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        httpGet.setURI(uri);
        String html = getRespJSONByRequest(httpGet);
        return html;
    }
    private static void saveHtml( String html) {
        String savefile = "D://html.txt";
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(savefile);
            fwriter.write(html);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static String getFromLoacalFile(){
        StringBuilder sb = new StringBuilder();
        try {
//            String encoding="GBK";
            File file=new File( "D://html.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file));//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    sb.append(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return sb.toString();
    }
}
