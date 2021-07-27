package com.zzs.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author mm013
 * @Date 2021/5/20 14:40
 */
public class SendEmailUtils {
    public static String sendQQEmail(String email) throws MessagingException {
        // 收件人电子邮箱
        String to = email;
        // 发件人电子邮箱
        String from = "1418679522@qq.com";
        // 指定发送邮件的主机为 smtp.qq.com
        String host = "smtp.qq.com";  //QQ 邮件服务器
        // 获取系统属性
        Properties properties = new Properties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", "1418679522@qq.com");
        properties.put("mail.password", "qknspdnpxufmjiid");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
            }
        });

        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(session);
        // Set From: 头部头字段
        message.setFrom(new InternetAddress(from));
        // Set To: 头部头字段
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // Set Subject: 头部头字段
        message.setSubject("background-system");
        String emailCode = GeneralUtils.generalVerificationCode();
        // 设置消息体
        message.setText("尊敬的用户：\n 您的邮箱验证码为：" + emailCode + "\n 此验证码5分钟内有效");
        // 发送消息
        Transport.send(message);
        return emailCode;
    }
}
