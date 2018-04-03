package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.utils.FileDownloadUtils;

import net.sf.json.JsonConfig;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * ClassName:SubAreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月16日 上午9:41:27 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype") // spring 的注解,多例
public class SubAreaAction extends CommonAction<SubArea> {

    public SubAreaAction() {
        super(SubArea.class);
    }

    @Autowired
    private SubAreaService subAreaService;

    @Action(value = "subareaAction_save", results = {@Result(name = "success",
            location = "/pages/base/sub_area.html", type = "redirect")})
    public String save() {

        subAreaService.save(getModel());
        return SUCCESS;
    }

    @Action(value = "subAction_pageQuery")
    public String pageQuery() throws IOException {

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1

        Pageable pageable = new PageRequest(page - 1, rows);

        Page<SubArea> page = subAreaService.findAll(pageable);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});

        page2json(page, jsonConfig);
        return NONE;
    }

    // 查询未关联的分区
    @Action(value = "subAreaAction_findUnAssociatedSubAreas")
    public String findUnAssociatedSubAreas() throws IOException {

        List<SubArea> list = subAreaService.findUnAssociatedSubAreas();
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        list2json(list, jsonConfig);
        return NONE;
    }

    // 查询已关联的分区
    @Action(value = "subAreaAction_findAssociatedSubAreas")
    public String findAssociatedSubAreas() throws IOException {

        List<SubArea> list =
                subAreaService.findAssociatedSubAreas(getModel().getId());
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas","couriers"});
        list2json(list, jsonConfig);
        return NONE;
    }
    
    // 导出excel
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }
    
    @Action(value = "subareaAction_doExportAll")
    public String doExportAll() {
        try {
            List<SubArea> list = null;
            if (StringUtils.isNotEmpty(ids)) {
                list = subAreaService.findById(ids);
            }else {
                Page<SubArea> pages = subAreaService.findAll(null);
                list = pages.getContent();
            }


            HSSFWorkbook workbook = new HSSFWorkbook(); // 在内存中创建excel
            // 创建sheet
            HSSFSheet sheet = workbook.createSheet();

            sheet.setDefaultColumnWidth(20); // 设置列宽
            // sheet.setDefaultRowHeightInPoints(20); //设置行高

            // 创建标题行
            HSSFRow titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(30);
            HSSFCellStyle style = workbook.createCellStyle(); // 创建样式
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式

            HSSFFont headerFont = (HSSFFont) workbook.createFont(); // 创建字体样式
            headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
            headerFont.setFontName("黑体"); // 设置字体类型
            headerFont.setFontHeightInPoints((short) 15); // 设置字体大小
            style.setFont(headerFont); // 为标题样式设置字体样式

            String[] titledatas = {"分拣编号", "省", "市", "区", "关键字", "起始号", "终止号", "单双号", "辅助关键字"};

            for (int i = 0; i < titledatas.length; i++) {
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellValue(titledatas[i]);
                cell.setCellStyle(style);
            }

            // 创建内容
            HSSFCellStyle style2 = workbook.createCellStyle(); // 创建样式
            style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式

            // 创建标题行
            int aa = -1;
            HSSFRow bodyRow = sheet.createRow(1);
            for (SubArea subArea : list) {
                int lastRowNum = sheet.getLastRowNum(); // 获取最后一行的行号
                HSSFRow dataRow = sheet.createRow(lastRowNum + 1 + aa);
                aa = 0;
                String[] bodydatas = {subArea.getId() + "", subArea.getArea().getProvince(),
                        subArea.getArea().getCity(), subArea.getArea().getDistrict(),
                        subArea.getKeyWords(), subArea.getStartNum(), subArea.getEndNum(),
                        subArea.getSingle() + "", subArea.getAssistKeyWords()};

                for (int i = 0; i < bodydatas.length; i++) {
                    HSSFCell cell = dataRow.createCell(i);
                    cell.setCellValue(bodydatas[i]);
                    cell.setCellStyle(style2);
                }
            }

            // 文件名
            String filename = "分区数据统计.xls";

            // 一个流两个头
            HttpServletResponse response = ServletActionContext.getResponse();
            ServletContext servletContext = ServletActionContext.getServletContext();
            ServletOutputStream outputStream = response.getOutputStream();
            HttpServletRequest request = ServletActionContext.getRequest();

            // 先获取mimeType再重新编码,避免编码后后缀名丢失,导致获取失败
            String mimeType = servletContext.getMimeType(filename);
            // 获取浏览器的类型
            String userAgent = request.getHeader("User-Agent");

            // 对文件名重新编码
            filename = FileDownloadUtils.encodeDownloadFilename(filename, userAgent);

            // 设置信息头
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);

            // 写出文件
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return NONE;
    }

  //修改数据
    @Action(value="subareaAction_edit",results={@Result(name="success",location="/pages/base/sub_area.html",type="redirect")})
     public String edit(){
        subAreaService.save(getModel());
        return SUCCESS;
     }

}
