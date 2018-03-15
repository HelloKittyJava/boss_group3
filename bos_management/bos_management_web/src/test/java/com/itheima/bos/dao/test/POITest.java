package com.itheima.bos.dao.test;

import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * ClassName:POITest <br/>
 * Function: <br/>
 * Date: 2018年3月15日 上午10:51:37 <br/>
 */
public class POITest {

    public static void main(String[] args) throws Exception {
        // 读取文件
        HSSFWorkbook workbook = new HSSFWorkbook(
                new FileInputStream("C:\\Users\\Alpha\\Desktop\\a.xls"));

        // 获取工作簿
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 代表一行
        for (Row row : sheet) {
            int rowNum = row.getRowNum();
            if (rowNum == 0) {
                continue;
            }

            for (Cell cell : row) {
                String value = cell.getStringCellValue();
                System.out.print(value + "\t");
            }
            System.out.println();
        }
        // 释放资源
        workbook.close();

    }

}
