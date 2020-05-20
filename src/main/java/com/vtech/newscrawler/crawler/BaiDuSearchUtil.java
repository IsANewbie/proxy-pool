package com.vtech.newscrawler.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BaiDuSearchUtil {

    /**
     * 根据searchKey 爬取相关的文章内容
     * @param searchKey
     * @return
     * @throws URISyntaxException
     */
    public static List<SearchData> search(String searchKey) throws URISyntaxException {

        List<SearchData> allSearchData = new ArrayList<SearchData>();
        int pageNumber = 1;
        boolean flag = true;
        while(pageNumber<30) {

            String searchInfoHtml = getSearchInfoHtml(searchKey,pageNumber);

           // boolean isEndpage = isEndPage(searchInfoHtml);
            if(!false) {
                List<SearchData> currentPageSearchDataList = parseDataHtml(searchInfoHtml);
                allSearchData.addAll(currentPageSearchDataList);
            }else {
                flag = false;
            }
            pageNumber++;
        }
        return allSearchData;
    }

    /**
     *  判断当前搜索结果是否是最后一页
     * @param searchInfoHtml
     * @return
     */
    private static boolean isEndPage(String searchInfoHtml) {
        Document doc = Jsoup.parse(searchInfoHtml);
        Element pageElement = doc.select("div#page").get(0);

        String html = pageElement.html();
        if(html.indexOf("下一页")!=-1) {
            return false;
        }
        return true;
    }
    /**
     *  解析搜索结果中文章标题和文章的url
     * @param searchInfoHtml
     * @return
     */
    private static List<SearchData> parseDataHtml(String searchInfoHtml) {
        List<SearchData> searchDataList = new ArrayList<SearchData>();
        Document doc = Jsoup.parse(searchInfoHtml);

        Elements select = doc.select("a[data-click]");
        for (Element element : select) {
            String url = element.attr("href");
            if(!"javascript:;".equals(url)) {
                String title = element.html().replace("<em>", "").replace("</em>", "");
                if(!"百度快照".equals(title)) {
                    SearchData searchData = new SearchData();
                    searchData.setTitle(title);
                    searchData.setUrl(url);
                    searchDataList.add(searchData);
                }
            }
        }

        return searchDataList;
    }
    /**
     *  爬取百度搜索具体页数结果页面
     * @param searchKey :搜索的关键词
     * @param number:爬取的页数
     * @return
     * @throws URISyntaxException
     */
    private static String getSearchInfoHtml(String searchKey,Integer pageNumber) throws URISyntaxException {
        String searchInfoHtml = "";

        URI uriParma = dualWithParameter(searchKey,pageNumber);
        HttpGet httpGet = new HttpGet(uriParma);
        addHeaders(httpGet);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            int satausCode = response.getStatusLine().getStatusCode();
            if(satausCode == 200 ){
                searchInfoHtml = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchInfoHtml;
    }
    /**
     * 设置httpGet的头部信息
     * @param httpGet
     */
    private static void addHeaders(HttpGet httpGet) {
        httpGet.addHeader("Host","www.baidu.com");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
    }
    /**
     * 处理百度搜索必须的查询参数
     * @param searchKey
     * @param pageNumber
     * @return
     * @throws URISyntaxException
     */
    private static URI dualWithParameter(String searchKey, Integer pageNumber) throws URISyntaxException {
        URI uri = new URI("https://www.baidu.com/s");
        URIBuilder uriBuilder = new URIBuilder(uri);
        uriBuilder.setParameter("wd", searchKey);//查询关键字
        uriBuilder.setParameter("pn", ((pageNumber-1)*10)+"");//显示结果页数
        uriBuilder.setParameter("oq", searchKey);//上次索引关键字
        uriBuilder.setParameter("ie", "utf-8");//关键字编码格式
        uriBuilder.setParameter("rsv_idx", "1");//
        uriBuilder.setParameter("f", "8");//用户自立搜索，透露表现用户直接点击“百度一下”按键
        uriBuilder.setParameter("rsv_bq", "1");
        uriBuilder.setParameter("tn", "baidu");
        URI uriParma = uriBuilder.build();
        return uriParma;
    }

    public static void main(String[] args) throws URISyntaxException {
        List<SearchData> allSearchData = BaiDuSearchUtil.search("瑞金证券");
        System.out.println(allSearchData.size());
    }


}

class SearchData {

    private String title;//文章标题
    private String url;//文章的url
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}