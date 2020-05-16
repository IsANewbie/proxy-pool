package com.vtech.newscrawler.job.crawler;


import com.vtech.newscrawler.entity.WebPage;

/**
 * @author chenerzhu
 * @create 2018-09-02 13:40
 **/
public interface ICrawler {
    WebPage getPage();

    void parsePage(WebPage webPage);
}