package com.vtech.newscrawler.crawler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BaseCrawl {
//    @Resource
//    IProxyIpRedisService proxyIpRedisService;
    public static String getRespJSONByRequest(HttpGet request){
        String result = null;
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {

            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);
            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity, "utf-8");
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(request.getURI().toString());
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(request.getURI().toString());
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }

        return result;
    }

    public static CloseableHttpResponse getTroken(String host){
        String result = null;
        HttpGet httpGet = new HttpGet(host);
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(httpGet);

            //4.判断响应状态为200，进行处理
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity, "utf-8");
            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            //  HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }

        return response;
    }

//    protected HttpGet setProxy(HttpGet request){
//        ProxyIp proxyIp = proxyIpRedisService.getOneRt();
//        System.out.println(proxyIp.toString());
//        HttpHost proxy = new HttpHost(proxyIp.getIp(),proxyIp.getPort());
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setProxy(proxy)
//                .setConnectTimeout(10000)
//                .setSocketTimeout(10000)
//                .setConnectionRequestTimeout(10000)
//                .build();
//        request.setConfig(requestConfig);
//        return request;
//    }
}
