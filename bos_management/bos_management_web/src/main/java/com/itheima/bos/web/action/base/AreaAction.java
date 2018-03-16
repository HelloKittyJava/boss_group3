package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.utils.PinYin4jUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * ClassName:AreaServiceAction <br/>
 * Function: <br/>
 * Date: 2018年3月15日 上午11:32:25 <br/>
 */
// 共性的代码抽取到父类
// 个性的实现由子类来完成
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends CommonAction<Area> {

    public AreaAction() {
        super(Area.class);
    }

    @Autowired
    private AreaService areaService;

    // 使用属性驱动获取用户上传的文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Action(value = "areaAction_importXLS", results = {@Result(name = "success",
            location = "/pages/base/area.html", type = "redirect")})
    public String importXLS() {

        try {
            HSSFWorkbook hssfWorkbook =
                    new HSSFWorkbook(new FileInputStream(file));
            // 读取第一个工作簿
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            // 储存对象的集合
            List<Area> list = new ArrayList<>();
            for (Row row : sheet) {
                // 跳过第一行
                if (row.getRowNum() == 0) {
                    continue;
                }
                // 读取数据
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();
                // 截掉最后一个字符
                province = province.substring(0, province.length() - 1);
                city = city.substring(0, city.length() - 1);
                district = district.substring(0, district.length() - 1);
                // 获取城市编码和简码
                String citycode =
                        PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
                String[] headByString = PinYin4jUtils
                        .getHeadByString(province + city + district);
                String shortcode =
                        PinYin4jUtils.stringArrayToString(headByString);
                // 封装数据
                Area area = new Area();
                area.setProvince(province);
                area.setCity(city);
                area.setDistrict(district);
                area.setPostcode(postcode);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);
                // 添加到集合
                list.add(area);

            }
            // 执行保存
            areaService.save(list);

            // 释放资源
            hssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    // AJAX请求不需要跳转页面
    @Action(value = "areaAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<Area> page = areaService.findAll(pageable);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});

        page2json(page, jsonConfig);

        return NONE;
    }

    private String q;

    public void setQ(String q) {
        this.q = q;
    }

    @Action(value = "areaAction_findAll")
    public String findAll() throws IOException {
        List<Area> list;
        if (StringUtils.isNotEmpty(q)) {
            // 根据用户输入的条件进行模糊匹配
            list = areaService.findByQ(q);
        } else {
            // 查询所有
            Page<Area> page = areaService.findAll(null);

            list = page.getContent();
        }

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});

        list2json(list, jsonConfig);

        return NONE;
    }
}
