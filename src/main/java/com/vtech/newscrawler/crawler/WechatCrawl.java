package com.vtech.newscrawler.crawler;

import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.util.VeDate;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WechatCrawl extends BaseCrawl{
    private static final String URL_PRI = "https://mp.weixin.qq.com/s";
    static HttpGet request = new HttpGet();
    static Map<String,String> param = new HashMap<>();
    public static void init() {
        //设置请求头
//        request.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.setHeader("User-Agent", " Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
//        request.setHeader("Connection", "keep-alive");
//        request.setHeader("Accept-Language","zh-CN,zh;q=0.9");
//        request.setHeader("Accept-Encoding","gzip, deflate, br");
//        request.setHeader("Host", "weixin.sogou.com");
//        request.setHeader("Content-Type","application/json");
//        request.setHeader("Cookie","ABTEST=4|1589334881|v1; SNUID=F57A772F989C3DE444907DAC9990659E; IPLOC=CN4403; SUID=6DE3EEB72423910A000000005EBB5361; SUID=6DE3EEB74F18910A000000005EBB5362; SUV=000A177EB7EEE36D5EBB5362E8358573; weixinIndexVisited=1; sct=4; UM_distinctid=17212706dc45e6-0197bd46c5d20a-39624307-144000-17212706dc6958; JSESSIONID=aaa8AAa4jaPB4c1ne4Rgx; Hm_lvt_751036519e4a758fea7041111c48b794=1589447258,1589447603; PHPSESSID=8lba80mdpnbehtn6of4jg9a746; CNZZDATA1272960299=640636450-1589443183-%7C1589448583; Hm_lpvt_751036519e4a758fea7041111c48b794=1589452105");

        //设置固定请求参数

        param.put("type","2");
        param.put("ie","utf8");
        param.put("_sug_","y");
        param.put("s_from","input");
        param.put("_sug_type_","");

    }

    public List<ExcelData> getNews(String keywords){
        int pn =1;
        List<ExcelData> excelDataList = new ArrayList<>();
        String html = "";
        init();
        param.put("query",keywords);
        boolean flag = true;
//        request = setProxy(request);
        while (flag && pn <= 10){
            param.put("page",pn+"");
            String urlStr = "https://weixin.sogou.com/weixin";
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
                html = getRespJSONByRequest(request);
                if(isEmpty(html)){
                    request = setProxy(request);
                    html = getRespJSONByRequest(request);
                }
                excelDataList.addAll(getNewsByHtml(html,keywords));
                pn++;
                if(!continueCrawl(html,keywords)){
                    flag = false;
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return excelDataList;
    }

    public static boolean continueCrawl(String html, String keyWord) {
        Document doc = Jsoup.parse(html);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date OneYearAgo = c.getTime();
        boolean canContinue = true;
        Elements newsParent = doc.getElementsByClass("news-list");
        Elements news = newsParent.first().getElementsByTag("li");
        for (Element element : news) {
            Element textBox = element.getElementsByClass("txt-box").first();
            Element TitleAndUrl = textBox.getElementsByTag("h3").first();
            Element dateAndMedia = textBox.select(".s-p").first();
            if (Objects.isNull(TitleAndUrl)) {
                TitleAndUrl = element.getElementsByTag("h4").first();
            }
            String date = dateAndMedia.select("span").html();
            String title = TitleAndUrl.select("a").text();
            canContinue = title.contains(keyWord);

        }
        return canContinue;
    }
    public static List<ExcelData> getNewsByHtml(String html,String keyword){
        List<ExcelData> excelDataList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements newsBox = doc.getElementsByClass("news-box");
        Elements newsParent = newsBox.first().getElementsByClass("news-list");
        Elements news = newsParent.first().getElementsByTag("li");
        for (Element element:news){
            ExcelData excelData = new ExcelData();
            Element textBox = element.getElementsByClass("txt-box").first();
            Element TitleAndUrl = textBox.getElementsByTag("h3").first();
            Element dateAndMedia = textBox.select(".s-p").first();
            if(Objects.isNull(TitleAndUrl)){
                TitleAndUrl = element.getElementsByTag("h4").first();
            }
            String title = TitleAndUrl.select("a").text();
            if(title.contains(keyword)){
                String aHref = TitleAndUrl.select("a").attr("href");
                String media = dateAndMedia.select("a").html();
                String date = dateAndMedia.select("span").html();
                excelData.setChannel("微信文章");
                excelData.setUrl(URL_PRI+aHref);
                excelData.setKeyword(keyword);
                excelData.setTitle(title);
                excelData.setMedia(media);
//                excelData.setDate(parseDate(date));
                excelDataList.add(excelData);
            }

        }
        return excelDataList;
    }

    private static String parseDate(String datestr){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(datestr);
        String timestampStr = m.replaceAll("").trim();
        timestampStr = StringUtils.isEmpty(timestampStr) ? "0":timestampStr;
        long timeStamp = Long.parseLong(timestampStr);
        Timestamp timestamp = new Timestamp(timeStamp);
        Date date = new Date(timestamp.getTime());
        return VeDate.dateToStr(date);
    }

    private boolean isEmpty(String html){
        return html.contains("我们的系统检测到您网络中存在异常访问请求");
    }
    public static void main(String[] args) {
//        WechatCrawl wechatCrawl = new WechatCrawl();
//        System.out.println(wechatCrawl.getsss("信丰中学").size());
        System.out.println(parseDate("<script>document.write(timeConvert('1587081625'))</script>"));
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.add(Calendar.YEAR, -1);
//        Date OneYearAgo = c.getTime();
//        Date date = VeDate.strToDate("2019-10-10");
//        System.out.println(OneYearAgo.after(date));
    }
}
