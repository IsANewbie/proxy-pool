package com.vtech.newscrawler.service.impl;

import com.vtech.newscrawler.crawler.BaiduCrawl;
import com.vtech.newscrawler.entity.baidu.News;
import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.service.BaiduNewsSeervice;
import com.vtech.newscrawler.util.ExcelUtils;
import com.vtech.newscrawler.util.VeDate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class BaiduNewsSeerviceImpl implements BaiduNewsSeervice {

    @Resource
    private BaiduCrawl baiduCrawl;

    @Override
    public void createExcel(String keyWord) {
        List<News> news = baiduCrawl.getNews(keyWord);
        Map<String,List<ExcelData>> map = new HashMap<>();
        map.put("百度",POJOTrans(keyWord,news));
        ExcelUtils.insertData(map);
    }

    private List<ExcelData> POJOTrans(String keyWord,List<News> news){
        List<ExcelData> excelDataList = new ArrayList<>(news.size());
        news.forEach(o -> {
            ExcelData excelData = new ExcelData();
            excelData.setChannel("百度");
            Timestamp timestamp = new Timestamp(o.getTs());
            Date date = new Date(timestamp.getTime());
            excelData.setDate(VeDate.dateToStrLong(date));
            excelData.setMedia(o.getSite());
            excelData.setTitle(o.getTitle());
            excelData.setUrl(o.getUrl());
            excelData.setKeyword(keyWord);
            excelDataList.add(excelData);
        });
        return excelDataList;
    }
}
