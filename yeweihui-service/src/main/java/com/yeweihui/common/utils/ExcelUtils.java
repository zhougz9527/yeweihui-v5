package com.yeweihui.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateTime;
import io.netty.util.internal.StringUtil;

/**
 * Excel 工具类
 * 
 *
 * @author 朱晓龙
 * 2020年9月9日  上午10:29:42
 */
public class ExcelUtils {

	/**
	 * 默认字符编码
	 */
	public static String DefaultEncoding = "UTF-8";
	
	/**
	 * html 表格以Excel类型返回
	 */
	public static void hetmlTableToExcel(HttpServletResponse response,String content,String name){
		
		hetmlTableToExcel(response,content,name,DefaultEncoding);
	}
	/**
	 * html 表格以Excel类型返回
	 */
	public static void hetmlTableToExcel(HttpServletResponse response,String content,String name,String encoding){
		try {
			encoding = (StringUtil.isNullOrEmpty(encoding)?StringUtil.isNullOrEmpty(DefaultEncoding)?DefaultEncoding:"UTF-8":encoding);
			response.setCharacterEncoding(encoding);
			
			response.setHeader("Content-disposition", "attachment; filename="+(StringUtil.isNullOrEmpty(name)?DateTime.now().getTime():new String(name.getBytes(), "iso-8859-1"))+".xls");
			content = content.replaceAll("<html ", "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns=\"http://www.w3.org/TR/REC-html40\" ");
			
		/*	StringBuffer otherInfo = new StringBuffer();
			otherInfo.append("<meta name=\"ProgId\" content=\"Excel.Sheet\"/>");
			otherInfo.append("<!--[if gte mso 9]>");
			otherInfo.append("  <xml>");
			otherInfo.append("    <x:ExcelWorkbook>");
			otherInfo.append("      <x:ExcelWorksheets>");
			otherInfo.append("        <x:ExcelWorksheet>");
			otherInfo.append("          <x:WorksheetOptions>");
			otherInfo.append("          </x:WorksheetOptions>");
			otherInfo.append("        </x:ExcelWorksheet>");
			otherInfo.append("      </x:ExcelWorksheets>");
			otherInfo.append("    </x:ExcelWorkbook>");
			otherInfo.append("  </xml>");
			otherInfo.append("<![endif]-->");
			
			content = content.replace("</head>", otherInfo+"\r\n</head>");*/

			response.getWriter().write(content);
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
