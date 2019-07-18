package com.luochen.web.register.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils implements Runnable {

    /*=========收件人的信息==========*/
    private String email;// 收件人邮箱
    private String code;// 激活码
    /*=========初始化===============*/
    public MailUtils(String email, String code) {
        this.email = email;
        this.code = code;
    }
    /*========线程============*/
    @Override
    public void run() {
        /*------------整体流程-----------*/
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件

        String from = "jinrixiaozhan@163.com";// 发件人电子邮箱
        String host = "smtp.163.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
       // Properties properties = new Properties();
        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        //modified
      //  properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.socketFactory.port", "465");

        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try
        {
//QQ邮箱需要下面这段代码，163邮箱不需要
//            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//            sf.setTrustAllHosts(true);
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.ssl.socketFactory", sf);


            // 1.获取默认session对象（创建连接对象，连接到邮箱服务器）
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("jinrixiaozhan@163.com", "luo1329127421"); // 发件人邮箱账号、密码
                }
            });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(from));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 2.3设置邮件主题
            message.setSubject("你的男神给你发送了一封账号激活邮件");
            // 2.4设置邮件内容
            String content =
                    "<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接</h1><h3><a href='http://localhost:8080/RegisterWeb/ActiveServlet?code="
                            + code + "'>http://120.78.173.225:8080/activateAccount?code=" + code
                            + "</href></h3></body></html>";
            message.setContent(content, "text/html;charset=UTF-8");

            // 3.发送邮件
            Transport.send(message);
            System.out.println("邮件成功发送!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
