package com.itheima.activemq;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

import com.itheima.utils.MailUtils2;


/**  
 * 邮件中间件       
 */
@Component
public class SmailConsumer implements MessageListener{
    @Override
    public void onMessage(Message msg) {
        MapMessage mapMessage = (MapMessage) msg;
        try {
            String receiver = mapMessage.getString("receiver");
            String subject = mapMessage.getString("subject");
            String emailBody = mapMessage.getString("emailBody");
            System.out.println("发邮件执行...");
        
         // 发送激活邮件
         //   MailUtils.sendMail(receiver, subject, emailBody);
            MailUtils2.sendMail(receiver, subject, emailBody);
        } catch (Exception e) {
            e.printStackTrace();  
        }
    }
    
    
}
  
