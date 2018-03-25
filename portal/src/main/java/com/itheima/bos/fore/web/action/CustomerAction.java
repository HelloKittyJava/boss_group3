package com.itheima.bos.fore.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:CustomerAction <br/>
 * Function: <br/>
 * Date: 2018年3月19日 上午9:49:34 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CustomerAction extends ActionSupport
        implements ModelDriven<Customer> {
    private Customer model = new Customer();

    @Override
    public Customer getModel() {

        return model;
    }

    @Autowired
    private JmsTemplate jmsTemplate;

    // 发送验证码
    @Action(value = "customerAction_sendSMS")
    public String sendSMS() throws IOException {

        // 随机生成验证码
        final String code = RandomStringUtils.randomNumeric(6);
        System.out.println(code);
        // 储存验证码
        ServletActionContext.getRequest().getSession()
                .setAttribute("serverCode", code);

        // 发送验证码
        jmsTemplate.send("sms", new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage message = session.createMapMessage();
                message.setString("tel", model.getTelephone());
                message.setString("code", code);
                return message;
            }
        });

        return NONE;
    }

    // 使用属性驱动获取用户输入的验证码
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Action(value = "customerAction_regist",
            results = {
                    @Result(name = "success", location = "/signup-success.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String regist() {

        // 校验验证码
        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("serverCode");
        if (StringUtils.isNotEmpty(checkcode)
                && StringUtils.isNotEmpty(serverCode)
                && serverCode.equals(checkcode)) {
            // 注册
            WebClient.create(
                    "http://localhost:8180/crm/webService/customerService/save")
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON).post(model);
            // 生成的验证码
            String activeCode = RandomStringUtils.randomNumeric(32);
            // 存储验证码
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1,
                    TimeUnit.DAYS);
            String emailBody =
                    "感谢您注册本网站的帐号，请在24小时之内点击<a href='http://localhost:8280/portal/customerAction_active.action?activeCode="
                            + activeCode + "&telephone=" + model.getTelephone()
                            + "'>本链接</a>激活您的帐号";
            // 发送激活邮件
            MailUtils.sendMail(model.getEmail(), "激活邮件", emailBody);

            return SUCCESS;
        }
        return ERROR;
    }

    // 使用属性驱动获取激活码
    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value = "customerAction_active",
            results = {
                    @Result(name = "success", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/signup-fail.html",
                            type = "redirect")})
    public String active() {

        // 比对激活码
        String serverCode =
                redisTemplate.opsForValue().get(model.getTelephone());

        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(activeCode)
                && serverCode.equals(activeCode)) {
            // 判断用户是否已经激活
            // 激活
            WebClient.create(
                    "http://localhost:8180/crm/webService/customerService/active")
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).put(null);

            return SUCCESS;
        }

        return ERROR;

    }

    @Action(value = "customerAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "error", location = "/login.html",
                            type = "redirect"),
                    @Result(name = "unactived", location = "/login.html",
                            type = "redirect")})
    public String login() {

        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("validateCode");

        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(checkcode)
                && serverCode.equals(checkcode)) {

            // 校验用户是否激活
            Customer customer = WebClient.create(
                    "http://localhost:8180/crm/webService/customerService/isActived")
                    .type(MediaType.APPLICATION_JSON)
                    .query("telephone", model.getTelephone())
                    .accept(MediaType.APPLICATION_JSON).get(Customer.class);
            // 空指针异常
            // Integer int
            if (customer != null && customer.getType() != null) {
                if (customer.getType() == 1) {
                    // 激活了
                    // 登录
                    Customer c = WebClient.create(
                            "http://localhost:8180/crm/webService/customerService/login")
                            .type(MediaType.APPLICATION_JSON)
                            .query("telephone", model.getTelephone())
                            .query("password", model.getPassword())
                            .accept(MediaType.APPLICATION_JSON)
                            .get(Customer.class);

                    if (c != null) {
                        ServletActionContext.getRequest().getSession()
                                .setAttribute("user", c);

                        return SUCCESS;
                    } else {
                        return ERROR;
                    }

                } else {
                    // 用户已经注册成功，但是没有激活
                    return "unactived";
                }
            }
        }

        return ERROR;

    }

}
