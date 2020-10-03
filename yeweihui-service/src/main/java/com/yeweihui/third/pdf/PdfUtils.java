package com.yeweihui.third.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.yeweihui.modules.operation.entity.RequestEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

@Component
public class PdfUtils {
    @Value("${FONT}")
    private String font;
    private static Configuration freemarkerCfg = null;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    private void init() {
        // 获取模板,并设置编码方式
        setFreemarkerCfg();
    }

    public void createPdf(String content, String dest, String fileName) throws IOException, DocumentException {
        File f = new File(dest);
        if (!f.exists()) {
            f.mkdirs();
        }

        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest+fileName));
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(font);
//        logger.info("XMLWorkerHelper.getInstance():{}",XMLWorkerHelper.getInstance());
//        logger.info("Charset.forName(\"UTF-8\"):{}",Charset.forName("UTF-8"));
//        logger.info("content.getBytes(\"UTF-8\"):{}",content.getBytes("UTF-8"));
        logger.info("writer:{}, document:{}, fontImp:{}, font:{}, XMLWorkerHelper.getInstance():{}，Charset.forName(\"UTF-8\"):{}, content.getBytes(\"UTF-8\"):{}",
                writer, document, fontImp, font, XMLWorkerHelper.getInstance(), Charset.forName("UTF-8"), content.getBytes("UTF-8"));

        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(content.getBytes("UTF-8")), null, Charset.forName("UTF-8"), fontImp);
        // step 5
        document.close();

    }

    /**
     * freemarker渲染html
     */
    public String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
        Writer out = new StringWriter();

        try {
            Template template = freemarkerCfg.getTemplate(htmlTmp);
            template.setEncoding("UTF-8");
            //将合并后的数据和模板写入到流中，这里使用的字符流
            template.process(data, out);
            out.flush();
            return out.toString();
        } catch (Exception e) {
            logger.error("freemarker process error. template={}", htmlTmp, e);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置freemarkerCfg
     */
    private void setFreemarkerCfg() {
        freemarkerCfg = new Configuration();
        //freemarker的模板目录
        freemarkerCfg.setClassForTemplateLoading(this.getClass(), "/template");
    }

    /**
     * 读取本地pdf,这里设置的是预览
     */
    public void readPdf(HttpServletResponse response, String dest) {
        response.reset();
        response.setContentType("application/pdf");
        try {
            File file = new File(dest);
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
            response.setHeader("Content-Disposition", "inline; filename= file");
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
