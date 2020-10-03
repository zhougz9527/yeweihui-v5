package com.yeweihui.modules.common.service.impl;

import com.yeweihui.modules.common.service.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {


    @Override
    public void write(HttpServletResponse response, String sheetName, String fileName, List<List<String>> lists) throws Exception {
        Workbook wb = new HSSFWorkbook();
        CreationHelper helper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(sheetName);
        if (null != lists && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                Row row = sheet.createRow(i);
                List<String> values = lists.get(i);
                if (null != values && values.size() > 0) {
                    for (int j = 0; j < values.size(); j++) {
                        String value = null == values.get(j) ? "" : values.get(j);
                        row.createCell(j).setCellValue(helper.createRichTextString(value));
                    }
                }
            }
        }

        fileName = new String(fileName.getBytes("ISO-8859-1"), "utf-8"); // 转中文乱码
        fileName = URLEncoder.encode(fileName, "utf-8"); // 符合 RFC 6266 标准

        response.setContentType("application/octet-stream");
        response.addHeader("content-type", "application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        response.flushBuffer();
        //将workbook中的内容写入输出流中
        wb.write(response.getOutputStream());
    }


}
