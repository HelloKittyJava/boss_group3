package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.service.system.MenuService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:MenuAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午9:18:36 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class MenuAction extends CommonAction<Menu> {

    public MenuAction() {
        super(Menu.class);
    }

    @Autowired
    private MenuService menuService;

    @Action(value = "menuAction_findLevelOne")
    public String findLevelOne() throws IOException {
        List<Menu> list = menuService.findLevelOne();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(
                new String[] {"roles", "childrenMenus", "parentMenu"});
        list2json(list, jsonConfig);
        return NONE;
    }

    @Action(value = "menuAction_save", results = {@Result(name = "success",
            location = "/pages/system/menu.html", type = "redirect")})
    public String save() {

        menuService.save(getModel());
        return SUCCESS;
    }

}
