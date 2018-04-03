package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.domain.take_delivery.WayBill;
import com.itheima.bos.service.take_delivery.WayBillService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.utils.FileDownloadUtils;
import com.itheima.utils.PinYin4jUtils;

import net.sf.json.JsonConfig;

/**
 * ClassName:WaybillAction <br/>
 * Function: <br/>
 * Date: 2018年3月25日 上午11:15:01 <br/>
 */
@Namespace("/") 
@ParentPackage("struts-default")
@Controller 
@Scope("prototype")//17:40
public class WaybillAction extends CommonAction<WayBill> {

    public WaybillAction() {
        super(WayBill.class);
    }

    @Autowired
    private WayBillService wayBillService;

    @Action("waybillAction_save")
    public String save() throws IOException {
    	List<WayBill> list = new ArrayList<>();
        String msg = "0";
        try {
            list.add(getModel());
            wayBillService.save(list);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "1";
        }

        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(msg);
        return NONE;
    }
    
    
    /**
     * 导入运单
     * @return
     */
    // 使用属性驱动获取用户上传的文件
    private File file;
    public void setFile(File file) {
        this.file = file;
    }
    @Action(value = "waybillAction_importXLS", results = {@Result(name = "success",
            location = "/pages/take_delivery/waybill_import.html", type = "redirect")})
    public String importXLS() {

        try {
            HSSFWorkbook hssfWorkbook =
                    new HSSFWorkbook(new FileInputStream(file));
            // 读取第一个工作簿sheet
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            // 储存对象的集合
            List<WayBill> list = new ArrayList<>();
            for (Row row : sheet) {
                // 跳过第一行
                if (row.getRowNum() == 0) {
                    continue;
                }
                // 读取数据
                String goodsType = row.getCell(0).getStringCellValue();
                String sendProNum = row.getCell(1).getStringCellValue();
                String sendName = row.getCell(2).getStringCellValue();
                
                Cell cell04 = row.getCell(3);
                cell04.setCellType(Cell.CELL_TYPE_STRING);
                String sendMobile =  cell04.getStringCellValue();
                
                String sendAddress = row.getCell(4).getStringCellValue();
                String recName = row.getCell(5).getStringCellValue();
                
                Cell cell07 = row.getCell(6);
                cell07.setCellType(Cell.CELL_TYPE_STRING);
                String recMobile = cell07.getStringCellValue();
                
                String recCompany = row.getCell(7).getStringCellValue();
                String recAddress = row.getCell(8).getStringCellValue();
               
                // 封装数据
                WayBill wayBill = new WayBill();
                wayBill.setGoodsType(goodsType);
                wayBill.setSendProNum(sendProNum);
                wayBill.setSendName(sendName);
                wayBill.setSendMobile(sendMobile);
                wayBill.setSendAddress(sendAddress);
                wayBill.setRecName(recName);
                wayBill.setRecMobile(recMobile);
                wayBill.setRecCompany(recCompany);
                wayBill.setRecAddress(recAddress);
                
                // 添加到集合
                list.add(wayBill);

            }
            // 执行保存
            wayBillService.save(list);

            // 释放资源
            hssfWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }
    
    
    /**
     * 查询所有运单
     * @throws IOException 
     */
    @Action(value = "waybillAction_pageQuery")
    public String pageQuery() throws IOException{
    	Pageable pageable = new PageRequest(page-1, rows);
    	Page<WayBill> page = wayBillService.findAll(pageable);
		JsonConfig jsonConfig = new JsonConfig();
	    jsonConfig.setExcludes(new String[] {"order"});
	    page2json(page, jsonConfig);
    	
    	return NONE;
    }
    
    /**
     * 模版下载
     * @return
     * @throws IOException 
     */
    @Action(value = "waybillAction_downloadTemplete")
    public String downloadTemplete() throws IOException{
    	
    	// 在内存中创建了一个excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet();
        // 创建标题行
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.createCell(0).setCellValue("托寄物类型");
        titleRow.createCell(1).setCellValue("快递产品类型");
        titleRow.createCell(2).setCellValue("发件人姓名");
        titleRow.createCell(3).setCellValue("发件人电话");
        titleRow.createCell(4).setCellValue("发件人地址");
        titleRow.createCell(5).setCellValue("收件人姓名");
        titleRow.createCell(6).setCellValue("收件人电话");
        titleRow.createCell(7).setCellValue("收件人公司");
        titleRow.createCell(8).setCellValue("收件人地址");
    	
    	
        // 文件名
        String filename = "导入运单模版.xls";

        // 一个流两个头
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletContext servletContext =
                ServletActionContext.getServletContext();
        ServletOutputStream outputStream = response.getOutputStream();
        HttpServletRequest request = ServletActionContext.getRequest();

        // 获取mimeType
        // 先获取mimeType再重新编码,避免编码后后缀名丢失,导致获取失败
        String mimeType = servletContext.getMimeType(filename);
        // 获取浏览器的类型
        String userAgent = request.getHeader("User-Agent");
        // 对文件名重新编码
        filename =
                FileDownloadUtils.encodeDownloadFilename(filename, userAgent);

        // 设置信息头
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition",
                "attachment; filename=" + filename);

        // 写出文件
        workbook.write(outputStream);
        workbook.close();
        return NONE;
    }
    
}
