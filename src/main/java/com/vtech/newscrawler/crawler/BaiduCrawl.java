package com.vtech.newscrawler.crawler;

import com.alibaba.fastjson.JSON;
import com.vtech.newscrawler.entity.baidu.News;
import com.vtech.newscrawler.entity.baidu.Root;
import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.util.UicodeBackslashU;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BaiduCrawl extends BaseCrawl {
    static HttpGet request = new HttpGet();
    static Map<String,String> param = new HashMap<>();
    private static final String[] TITLE_REGX = {"-","|"};
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
//        param.put("rn","20");
//        param.put("sv","8.3.3");
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
        init();
        param.put("word",keywords);
        boolean flag = true;
        while (flag){
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
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }


        }
        return news;
    }

//    public static void main(String[] args) throws URISyntaxException {
//        List<ExcelData> excelData = getBaiduNews("瑞金证券");
//        ExcelUtils.createExcel();
//        HashMap<String,List<ExcelData>> params = new HashMap<>();
//        params.put("百度",excelData);
//        ExcelUtils.insertData(params);
////        excelData.forEach(o -> {
////            System.out.println(o.toString());
////        });
//        System.out.println(excelData.size());
//    }

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
                if(!Objects.isNull(timeSpan)){
                    String time = timeSpan.text();
//                    Element a = result.select("a").first();
                    Element mediaEle = result.select(".nor-src-wrap").first();
//                    String title = a.text();
                    String url = a.attr("href");
                    url = getRealUrlFromBaiduUrl(url);
//                    System.out.println(title);
                    if(title.contains(keyWord) && !title.contains("百度知道")){
                        ExcelData excelData = new ExcelData();
                        String Temphtml = getHtml(url);
                        Document document = Jsoup.parse(Temphtml);
                        Elements titleEle =  document.getElementsByTag("title");
                        title = titleEle.isEmpty() ? title : titleEle.first().text();
                        System.out.println(title);
                        if(title.contains("|")){
                            title = title.split("\\|")[0];
                        }else if(title.contains("_")){
                            title = title.split("_")[0];
                        }else if(title.contains("-")){
                            title = title.split("-")[0];
                        }else if(title.contains("�")){
                            title = "标题乱码，建议手动输入";
                        }
                        System.out.println(title);
                        String media = Objects.isNull(mediaEle) ? "" : mediaEle.text();
                        excelData.setChannel("百度");
                        excelData.setKeyword(keyWord);
                        excelData.setMedia(media);
                        excelData.setTitle(title);
                        excelData.setUrl(url);
                        excelData.setDate(time);
                        newsResult.add(excelData);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private static String getBaiduSearch() throws URISyntaxException {
        BaiduCrawl baiduCrawl = new BaiduCrawl();
        baiduCrawl.init();
        URI uri = new URI("https://www.baidu.com/s?word=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8&pn=10");
        request.setURI(uri);
        String html = BaseCrawl.getRespJSONByRequest(request);
        return html;
    }
    private static String getHtml(String url){
        BaiduCrawl baiduCrawl = new BaiduCrawl();
        baiduCrawl.init();
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        request.setURI(uri);
        String html = BaseCrawl.getRespJSONByRequest(request);
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
