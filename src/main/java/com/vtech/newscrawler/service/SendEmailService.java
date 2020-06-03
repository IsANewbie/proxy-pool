package com.vtech.newscrawler.service;/**
 * @author zoubin
 * @create 2020/5/16 - 16:21
 */

import com.vtech.newscrawler.util.ExcelUtils;
import com.vtech.newscrawler.util.VeDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 *@ClassName SendEmailService
 *@Description TODO
 *@Author 邹斌
 *@Date 2020/5/16 16:21
 **/
@Service
public class SendEmailService {
    @Autowired
    JavaMailSender jms;

    private final static String RESVICER = "zhengsq@qhee.com";
//    private final static String RESVICER = "332280454@qq.com";
    private final static String SENDER = "1174475761@qq.com";
//    private final static String SENDER = "zoubin_1996@163.com";

    public String send(String sender,String receiver,String title,String text){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送方
        mainMessage.setFrom(sender);
        //接收方
        mainMessage.setTo(receiver);
        //发送的标题
        mainMessage.setSubject(title);
        //发送的内容
        mainMessage.setText(text);
        jms.send(mainMessage);
        return "success";
    }

    /**
     * 发送含有附件的邮件
     * @return
     * @throws
     */
    public String sendAttachment(String keyword, String resvicer) throws MessagingException, MessagingException {
        MimeMessage message = jms.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        String filePath = ExcelUtils.NEW;
        String content = VeDate.dateToStrLong(new Date())+ "关键词"+keyword+"舆情信息";
        //发送方
        helper.setFrom(SENDER);
        //接收方
        helper.setTo(resvicer);
        //邮件主题
        helper.setSubject(content);
        //邮件内容
        helper.setText(content,true);
        //邮件的附件
        FileSystemResource file = new FileSystemResource(new File(filePath));
        //获取附件的文件名
        content = VeDate.dateToStrLong(new Date())+keyword+"舆情信息";
        String fileName = content+".xlsx";
        helper.addAttachment(fileName,file);
        jms.send(message);
        System.out.println("发送成功");
        return "sendAttachment success";
    }

    public static void main(String[] args) {
//        String keyword = "瑞金证券";
//        String content = VeDate.dateToStrLong(new Date())+ "关键词"+keyword+"舆情信息";
//        System.out.println(content);
        String s = "http://money.163.com/13/0515/08/8UTDN35700253B0H.html#from=relevant#xwwzy_35_bottomnewskwd";
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(s);
    }
}
