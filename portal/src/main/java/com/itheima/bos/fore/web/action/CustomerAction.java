package com.itheima.bos.fore.web.action;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
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

    // 发送验证码
    @Action(value = "customerAction_sendSMS")
    public String sendSMS() throws IOException {

        // 随机生成验证码
        String code = RandomStringUtils.randomNumeric(6);

        System.out.println(code);
        // 储存验证码
        ServletActionContext.getRequest().getSession()
                .setAttribute("serverCode", code);
        // 发送验证码
        try {
            SmsUtils.sendSms(model.getTelephone(), code);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return NONE;
    }

}
