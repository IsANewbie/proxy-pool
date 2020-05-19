package com.vtech.newscrawler.service;

import com.vtech.newscrawler.entity.excel.ExcelData;

import java.util.List;

/**
 * @author zoubin
 * @create 2020/5/19 - 22:01
 */
public interface SinaService {
    List<ExcelData> getSinaNews(String keyWord);
}
