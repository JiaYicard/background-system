package com.zzs.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author mm013
 * @Date 2021/5/20 14:40
 */
public class SendEmailUtils {
    public static Integer sendQQEmail(String email) throws Exception {
        // 创建Properties 类用于记录邮箱的一些属性
        Properties properties = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        properties.put("mail.smtp.auth", true);
        //此处填写SMTP服务器
        properties.put("mail.smtp.host", "smtp.qq.com");
        //端口号
        properties.put("mail.smtp.port", "587");
        // 此处填写账号
        properties.put("mail.user", "1418679522@qq.com");
        // 此处的密码就是前面说的16位STMP口令
        properties.put("mail.password", "flbhvuekqhsvfhab");
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = properties.getProperty("mail.user");
                String password = properties.getProperty("mail.password");
                // 用户名、密码
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(properties, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(properties.getProperty("mail.user"));
        message.setFrom(form);
        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress(email);
        // 设置邮件标题
        message.setRecipient(Message.RecipientType.TO, to);
        // 创建附件“附件节点”
//        MimeBodyPart attachment = new MimeBodyPart();
        // 读取本地文件
//        DataHandler dh2 = new DataHandler(new FileDataSource("C:\\Users\\admin\\Desktop\\pb.pptx"));
        // 将附件数据添加到“节点”
//        attachment.setDataHandler(dh2);
        // 设置附件的文件名（需要编码）
//        attachment.setFileName(MimeUtility.encodeText(dh2.getName()));
        //设置整个邮件的关系(将最终的混合“节点”作为邮件的内容添加到邮件对象)
        MimeMultipart mm = new MimeMultipart();
//         mm.addBodyPart(attachment);
        //TODO 邮箱发送未完成
        // 混合关系
        mm.setSubType("mixed");
        message.setSubject("background-system");
        Integer emailCode = GeneralUtils.generalVerificationCode();
        // 设置邮件的内容体
        message.setContent("尊敬的用户：\n 您的邮箱验证码为：" + emailCode + "\n 此验证码5分钟内有效", "text/html;charset=UTF-8");
        message.setContent(mm);
        //保存设置
        message.saveChanges();
        //发送邮件
        Transport.send(message);
        return emailCode;
    }
}
