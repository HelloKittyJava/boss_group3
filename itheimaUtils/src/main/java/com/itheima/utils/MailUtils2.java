package com.itheima.utils;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.sun.mail.util.MailSSLSocketFactory;
/*
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4.7</version>
</dependency>
*/
public class MailUtils2 {
    // to:发送给谁的邮箱   subject:邮件标题    content:邮件内容
	public static void sendMail(String to, String subject, String content ) {

		String address = "17606030702@163.com"; // yhmpc@163.com
		String password = "JWJ13004849656";
		String smtp_host = "smtp." + address.replaceAll(".*@(.*)", "$1"); //smtp.163.com

		Properties props = new Properties();
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.auth", "true");

		if ("smtp.qq.com".equals(smtp_host)) {
			MailSSLSocketFactory sslFactory = null;
			try {
				sslFactory = new MailSSLSocketFactory();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			sslFactory.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.socketFactory", sslFactory);
			props.put("mail.smtp.ssl.enable", "true");
		}

		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(address));
			message.setRecipient(RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setContent(content, "text/html;charset=utf-8");
			Transport transport = session.getTransport();
			transport.connect(smtp_host, address, password);
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("邮件发送失败...");
		}
	}

}
