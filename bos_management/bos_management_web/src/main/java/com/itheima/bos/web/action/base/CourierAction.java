package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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

    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException {

        Specification<Courier> specification = new Specification<Courier>() {
            /**
             * 创建一个查询的where语句
             * 
             * @param root : 根对象.可以简单的认为就是泛型对象
             * @param cb : 构建查询条件
             * @return a {@link Predicate}, must not be {@literal null}.
             */
            @Override
            public Predicate toPredicate(Root<Courier> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {

                String courierNum = model.getCourierNum();
                String company = model.getCompany();
                String type = model.getType();
                Standard standard = model.getStandard();

                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotEmpty(courierNum)) {
                    // 如果工号不为空,构建一个等值查询条件
                    // where courierNum = "001"
                    // 参数二 : 具体的要比较的值
                    Predicate p1 =
                            cb.equal(root.get("courierNum").as(String.class),
                                    courierNum);
                    list.add(p1);
                }
                if (StringUtils.isNotEmpty(company)) {
                    // 如果公司不为空,构建一个模糊查询条件
                    // where company like "001"
                    // 参数二 : 具体的要比较的值
                    Predicate p2 = cb.like(root.get("company").as(String.class),
                            "%" + company + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotEmpty(type)) {
                    // 如果类型不为空,构建一个等值查询条件
                    // where courierNum = "001"
                    // 参数二 : 具体的要比较的值
                    Predicate p3 =
                            cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }

                if (standard != null) {
                    String name = standard.getName();
                    if (StringUtils.isNotEmpty(name)) {
                        // 连表查询,查询标准的名字
                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 = cb
                                .equal(join.get("name").as(String.class), name);
                        list.add(p4);
                    }
                }
                // 用户没有输入查询条件
                if (list.size() == 0) {
                    return null;
                }

                // 用户输入了查询条件
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                // 用户输入了多少个条件,就让多少个条件同时都满足
                Predicate predicate = cb.and(arr);

                return predicate;
            }
        };

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Courier> page = courierService.findAll(specification, pageable);

        long total = page.getTotalElements();
        List<Courier> content = page.getContent();

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("rows", content);

        // 灵活控制输出的内容
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas", "takeTime"});

        String json = JSONObject.fromObject(map, jsonConfig).toString();

        // 在实际开发的时候,为了提高服务器的性能,把前台页面不需要的数据都应该忽略掉
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

        return NONE;
    }

    // 使用属性驱动获取要删除的快递员的Id
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    // 批量删除
    @Action(value = "courierAction_batchDel",
            results = {@Result(name = "success",
                    location = "/pages/base/courier.html", type = "redirect")})
    public String batchDel() {
        courierService.batchDel(ids);
        return SUCCESS;
    }

}
