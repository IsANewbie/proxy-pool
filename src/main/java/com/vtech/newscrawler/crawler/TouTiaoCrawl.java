package com.vtech.newscrawler.crawler;/**
 * @author zoubin
 * @create 2020/5/18 - 23:48
 */

import org.apache.http.client.methods.HttpGet;

import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName TouTiaoCrawl
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/18 23:48
 **/
public class TouTiaoCrawl extends BaseCrawl {
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
}
