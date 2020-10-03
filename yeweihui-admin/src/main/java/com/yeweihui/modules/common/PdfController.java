package com.yeweihui.modules.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.third.pdf.PdfUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: kevin
 * @Date: 2018/11/16
 */
@RestController
public class PdfController {

    @Autowired
    PdfUtils pdfUtils;

    @RequestMapping(value = "helloPdf")
    public void showPdf(HttpServletResponse response) throws IOException, DocumentException {
        //需要填充的数据
        Map<String, Object> data = new HashMap<>(16);
        data.put("name", "kevin");

        String content = pdfUtils.freeMarkerRender(data,"request.ftl");
        //创建pdf
        pdfUtils.createPdf(content, "yeweihui-admin/target/", "HelloWorld_CN_HTML_FREEMARKER.pdf");
        // 读取pdf并预览
        pdfUtils.readPdf(response, "yeweihui-admin/target/HelloWorld_CN_HTML_FREEMARKER.pdf");
    }

}
