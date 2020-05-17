package com.vtech.newscrawler.service;

import com.vtech.newscrawler.entity.excel.ExcelData;

import java.util.List;

public interface BaiduNewsSeervice {
    void createExcel(String keyWord);
    List<ExcelData> getExcelData(String keyWord);
}
