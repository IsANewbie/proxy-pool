package com.vtech.newscrawler.service;/**
 * @author zoubin
 * @create 2020/5/16 - 14:28
 */

import com.vtech.newscrawler.entity.excel.ExcelData;

import java.util.List;

/**
 *@ClassName WeChatService
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/16 14:28
 **/
public interface WeChatService {
    List<ExcelData> getExcelData(String keyWord);
}
