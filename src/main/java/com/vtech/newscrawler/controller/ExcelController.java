package com.vtech.newscrawler.controller;/**
 * @author zoubin
 * @create 2020/5/16 - 14:45
 */

import com.vtech.newscrawler.entity.excel.ExcelData;
import com.vtech.newscrawler.service.BaiduNewsSeervice;
import com.vtech.newscrawler.service.SendEmailService;
import com.vtech.newscrawler.service.SinaService;
import com.vtech.newscrawler.service.WeChatService;
import com.vtech.newscrawler.util.ExcelUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName ExcelController
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/16 14:45
 **/
@Controller
public class ExcelController {
    @Resource
    private BaiduNewsSeervice baiduNewsSeervice;
    @Resource
    private WeChatService weChatService;
    @Resource
    private SendEmailService emailService;
    @Resource
    private SinaService sinaService;

    @RequestMapping(value = "/search")
    public String search(@RequestParam("keyword") String keyword, Model model) throws MessagingException {
        List<ExcelData> baiduNews = baiduNewsSeervice.getExcelData(keyword);
        List<ExcelData> wechatNews = weChatService.getExcelData(keyword);
        List<ExcelData> sinaNews = sinaService.getSinaNews(keyword);
        Map<String,List<ExcelData>> map = new HashMap<>();
        map.put("百度",baiduNews);
        map.put("微信公众号",wechatNews);
        map.put("新浪财经",sinaNews);
        ExcelUtils.insertData(map);
        emailService.sendAttachment(keyword);
        model.addAttribute("msg","生成Excel成功,已发送至您的邮箱,注意查收");
        return "search";
    }
}
