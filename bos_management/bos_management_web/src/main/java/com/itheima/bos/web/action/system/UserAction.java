package com.itheima.bos.web.action.system;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.User;
import com.itheima.bos.web.action.CommonAction;

/**
 * ClassName:UserAction <br/>
 * Function: <br/>
 * Date: 2018年3月26日 上午10:09:31 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class UserAction extends CommonAction<User> {

    public UserAction() {
        super(User.class);
    }

    // 用户输入的验证码
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Action(value = "userAction_login",
            results = {
                    @Result(name = "success", location = "/index.html",
                            type = "redirect"),
                    @Result(name = "login", location = "/login.html",
                            type = "redirect")})
    public String login() {

        String serverCode = (String) ServletActionContext.getRequest()
                .getSession().getAttribute("key");

        if (StringUtils.isNotEmpty(serverCode)
                && StringUtils.isNotEmpty(checkcode)
                && serverCode.equals(checkcode)) {

            // 主体,代表当前用户
            Subject subject = SecurityUtils.getSubject();
            // 使用代码校验权限
            // subject.checkPermission("");

            AuthenticationToken token = new UsernamePasswordToken(
                    getModel().getUsername(), getModel().getPassword());
            try {
                // 执行登录
                subject.login(token);

                // 方法的返回值由Realm中doGetAuthenticationInfo方法定义SimpleAuthenticationInfo对象的时候,第一个参数决定的
                User user = (User) subject.getPrincipal();
                ServletActionContext.getRequest().getSession()
                        .setAttribute("user", user);

                return SUCCESS;
            } catch (UnknownAccountException e) {
                // 用户名写错了
                e.printStackTrace();
                System.out.println("用户名写错了");
            } catch (IncorrectCredentialsException e) {
                // 用户名写错了
                e.printStackTrace();
                System.out.println("密码错误");
            } catch (Exception e) {
                // 用户名写错了
                e.printStackTrace();
                System.out.println("其他错误");
            }
        }

        return LOGIN;
    }

    @Action(value = "userAction_logout", results = {@Result(name = "success",
            location = "/login.html", type = "redirect")})
    public String logout() {

        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return SUCCESS;

    }

}
