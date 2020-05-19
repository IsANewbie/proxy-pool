package com.vtech.newscrawler.service.impl;/**
 * @author zoubin
 * @create 2020/5/19 - 22:01
 */

import com.vtech.newscrawler.crawler.SinaCrawl;
import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.service.SinaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *@ClassName SinaServiceImpl
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/19 22:01
 **/
@Service
public class SinaServiceImpl implements SinaService {
    @Resource
    private SinaCrawl sinaCrawl;

    @Override
    public List<ExcelData> getSinaNews(String keyWord) {
        List<ExcelData> excelDataList = sinaCrawl.getSinaNews(keyWord);
        return excelDataList;
    }
}
