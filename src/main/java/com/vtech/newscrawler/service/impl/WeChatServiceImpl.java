package com.vtech.newscrawler.service.impl;/**
 * @author zoubin
 * @create 2020/5/16 - 14:33
 */

import com.vtech.newscrawler.crawler.WechatCrawl;
import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.service.WeChatService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *@ClassName WeChatServiceImpl
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/16 14:33
 **/
@Service
public class WeChatServiceImpl implements WeChatService {
    @Resource
    private WechatCrawl wechatCrawl;

    @Override
    public List<ExcelData> getExcelData(String keyWord) {
        List<ExcelData> excelDataList = wechatCrawl.getNews(keyWord);
        return excelDataList;
    }
}
