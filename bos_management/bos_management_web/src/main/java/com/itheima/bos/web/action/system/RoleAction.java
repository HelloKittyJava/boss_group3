package com.itheima.bos.web.action.system;

import java.io.IOException;
import java.util.List;

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

import com.itheima.bos.domain.system.Permission;
import com.itheima.bos.domain.system.Role;
import com.itheima.bos.service.system.RoleService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

/**
 * ClassName:RoleAction <br/>
 * Function: <br/>
 * Date: 2018年3月28日 上午10:55:04 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class RoleAction extends CommonAction<Role> {

    public RoleAction() {
        super(Role.class);
    }

    @Autowired
    private RoleService roleService;

    @Action(value = "roleAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Role> page = roleService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"users", "permissions", "menus"});
        page2json(page, jsonConfig);
        return NONE;
    }

    // 使用属性驱动获取菜单和权限的ID
    private String menuIds;

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    private Long[] permissionIds;

    public void setPermissionIds(Long[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    @Action(value = "roleAction_save", results = {@Result(name = "success",
            location = "/pages/system/role.html", type = "redirect")})
    public String save() {

        roleService.save(getModel(), menuIds, permissionIds);
        return SUCCESS;
    }

    @Action(value = "roleAction_findAll")
    public String findAll() throws IOException {

        Page<Role> page = roleService.findAll(null);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"users", "permissions", "menus"});

        List<Role> list = page.getContent();
        list2json(list, jsonConfig);
        return NONE;
    }
}
