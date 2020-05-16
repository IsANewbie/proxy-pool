package com.vtech.newscrawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) throws InterruptedException {
        //https://www.baidu.com/s?ie=utf-8&cl=2&medium=0&rtt=1&bsst=1&rsv_dl=news_t_sk&tn=news&word=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8
        //发送 GET 请求
//        String s=HttpRequest.sendGet("https://news.baidu.com/sn/api/search?_cfrom=1099a&_cuid2=6DFACA12A288C56105E094F2002174BBD68D7B2A2OMTJGCKDCD&_from=1099a&_network=53_0&_ua=gavihgaMvC9Vrmqlk4D0toabs8UsLqqqB&_uid=Yu2KtYOq-8g1u2iJ0OHqi_a3StYNPvuc_O2f8gu9HugcuSiplavraqqHB&_ut=99mjqkyfs8zV9mYtfNvWioaGs8qlC&baiduid=825888965556c8a040d9986e485455cf51a7f56c&cen=uid_ua_ut&cuid=825888965556c8a040d9986e485455cf51a7f56c&mid=825888965556c8a040d9986e485455cf51a7f56c&os=iphone&pd=newsplus&pn=0&rn=20&s=瑞金证券&sv=8.3.3", "");
        //https://s.weibo.com/weibo?q=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8&nodup=1&Refer=SWeibo_box&page=2
        //https://s.weibo.com/weibo?q=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8&nodup=1&Refer=SWeibo_box
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String wobo = "https://s.weibo.com/weibo?q=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8";
        String weixin = "https://weixin.sogou.com/weixinwap?query=%E7%91%9E%E9%87%91%E8%AF%81%E5%88%B8&type=2&ie=utf8&_sug_=y&_sug_type_=&s_from=input";
        String mWeiBo = "https://api.weibo.cn/2/cardlist?aid=01A8ajN5SVMsOEULlucRMLzekS24PfZVXxaeI-NMdHlki1hyc.&c=weicoabroad&containerid=100103type%253D61%2526q%253D%25E7%2591%259E%25E9%2587%2591%25E8%25AF%2581%25E5%2588%25B8&count=10&from=1238293010&gsid=_2A25zv7XXDeRxGeFO71QS8SzMyD-IHXVu7U4frDV6PUJbkdAKLRWhkWpNQXzEKARa2yM6KmbbJj5xejejmSWccmO4&i=de46790&lang=zh_CN&page=1&s=963b63d3&ua=iPhone9%2C2_iOS13.3.1_Weibo_intl._3820_wifi&v_p=59";
        String html = HttpRequest.sendGet(weixin,null);
        Document doc = Jsoup.parse(html);
        System.out.println(html);
        Elements newsParent = doc.getElementsByClass("pic-list");
        System.out.println(newsParent.size());
        Elements news = newsParent.first().getElementsByTag("li");
        System.out.println(news.size());
        for (Element element:news){
            Element h4 = element.getElementsByTag("h4").first();
            String aHref = h4.select("a").attr("href");
            String title = h4.select("div").text();
            System.out.println("title:"+title);
            System.out.println("url:"+aHref);
        }

//        System.out.println(html);
//        Elements cards = doc.getElementsByClass("card");
//        for (Element element : cards){
//            System.out.println(element.text());
//        }

        /*int count = 1000;
        while (count > 0){
            Thread.sleep(1 * 300);
            count--;
            System.out.println(HttpRequest.sendGet(weixin, "").length());
            System.out.println(sdf.format(new Date()) + "--循环执行第" + (1000-count) + "次");
        }*/




//        System.out.println(HttpRequest.sendGet(wobo,null));
//        System.out.println(s);
//        System.out.println(UicodeBackslashU.unicodeToCn(s));
//        //发送 POST 请求
//        String sr=HttpRequest.sendPost("http://localhost:8080/Home/RequestPostString", "key=123&v=456");
//        System.out.println(sr);
    }
}