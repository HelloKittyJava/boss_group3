package com.itheima.bos.web.action.take_delivery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.itheima.utils.PinYin4jUtils;

import net.sf.json.JsonConfig;

/**
 * ClassName:WaybillAction <br/>
 * Function: <br/>
 * Date: 2018年3月25日 上午11:15:01 <br/>
 */
@Namespace("/") // 等价于struts.xml文件中package节点namespace属性
@ParentPackage("struts-default") // 等价于struts.xml文件中package节点extends属性
@Controller // spring 的注解,控制层代码
@Scope("prototype")
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
                String goodsType = row.getCell(1).getStringCellValue();
                String sendProNum = row.getCell(2).getStringCellValue();
                String sendName = row.getCell(3).getStringCellValue();
                
                Cell cell04 = row.getCell(4);
                cell04.setCellType(Cell.CELL_TYPE_STRING);
                String sendMobile =  cell04.getStringCellValue();
                
                String sendAddress = row.getCell(5).getStringCellValue();
                String recName = row.getCell(6).getStringCellValue();
                
                Cell cell07 = row.getCell(7);
                cell07.setCellType(Cell.CELL_TYPE_STRING);
                String recMobile = cell07.getStringCellValue();
                
                String recCompany = row.getCell(8).getStringCellValue();
                String recAddress = row.getCell(9).getStringCellValue();
               
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
    
}
