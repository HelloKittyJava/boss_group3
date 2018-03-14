package com.itheima.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * ClassName:CourierAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 上午11:37:22 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport
        implements ModelDriven<Courier> {

    private Courier model = new Courier();

    @Override
    public Courier getModel() {
        return model;
    }

    @Autowired
    private CourierService courierService;

    @Action(value = "courierAction_save", results = {@Result(name = "success",
            location = "/pages/base/courier.html", type = "redirect")})
    public String save() {

        courierService.save(model);
        return SUCCESS;
    }

}
