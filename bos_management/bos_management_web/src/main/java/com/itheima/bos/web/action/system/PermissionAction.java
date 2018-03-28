package com.itheima.bos.web.action.system;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.system.Menu;
import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.service.system.PermissionService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:PermissionAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:45:30 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class PermissionAction extends CommonAction<Permission> {

    public PermissionAction() {

        super(Permission.class);
    }

    @Autowired
    private PermissionService permissionService;

    @Action(value = "permissonAction_save", results = {@Result(name = "success",
            location = "/pages/system/permission.html", type = "redirect")})
    public String save() {

        permissionService.save(getModel());
        return SUCCESS;
    }

    // struts框架在封装数据的时候优先封装给模型对象的
    @Action(value = "permissonAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Permission> page = permissionService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"roles"});
        page2json(page, jsonConfig);
        return NONE;
    }
}
