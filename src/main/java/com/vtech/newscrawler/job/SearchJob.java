package com.vtech.newscrawler.job;/**
 * @author zoubin
 * @create 2020/5/21 - 21:47
 */

import com.vtech.newscrawler.service.BaiduNewsSeervice;
import com.vtech.newscrawler.service.SendEmailService;
import com.vtech.newscrawler.service.SinaService;
import com.vtech.newscrawler.service.WeChatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *@ClassName SearchJob
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/21 21:47
 **/
@Component
public class SearchJob {
    private BaiduNewsSeervice baiduNewsSeervice;
    @Resource
    private WeChatService weChatService;
    @Resource
    private SendEmailService emailService;
    @Resource
    private SinaService sinaService;

    @Scheduled(cron = "${time.fundInfo}")
    public void job(){

    }
}
