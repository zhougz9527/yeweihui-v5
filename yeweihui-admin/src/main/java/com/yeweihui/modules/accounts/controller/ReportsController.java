package com.yeweihui.modules.accounts.controller;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.ExcelUtils;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.accounts.service.AccountsService;
import com.yeweihui.modules.accounts.service.ReportsService;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.query.AccountsFinancialinformQueryParam;
import com.yeweihui.modules.vo.query.AccountsQueryParam;
import com.yeweihui.modules.vo.query.ReceiptsAndDisbursementsParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
	 * @作者： zss
	 * @生成时间：16:00 2020/8/21
	 * @方法说明：//报表
     *
	 */
@RestController
@RequestMapping("accounts/reports")
public class ReportsController {

	@Autowired
	PdfUtils pdfUtils;
	@Autowired
	ReportsService reportsService;
	@Autowired
	AccountsService accountsService;
	@Autowired
	private ZonesService zonesService;

	/**
	 * 首页收支报表月度结余
	 */

	@RequestMapping("/blance")
	public R blance() {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		Map map = reportsService.blance(zoneId);
		return R.ok().put("data", map);
	}
	/***
		 * @作者： zss
		 * @生成时间：10:01 2020/9/8
		 * @方法说明：//月度报表首页
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@RequestMapping("/monthStatistical")
	public R monthStatistical(@DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		return R.ok().put("data", reportsService.monthOrYearStatistical(zoneId, statisticalDate, false));
	}

	/***
	 * @作者： zss
	 * @生成时间：10:01 2020/9/8
	 * @方法说明：//年度报表首页
	 * @参数：[statisticalDate]
	 * @返回值：com.yeweihui.common.utils.R
	 */
	@RequestMapping("/yearStatistical")
	public R yearStatistical(@DateTimeFormat(pattern = "yyyy") Date statisticalDate) {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		return R.ok().put("data", reportsService.monthOrYearStatistical(zoneId, statisticalDate, true));
	}

	/***
		 * @作者： zss
		 * @生成时间：16:29 2020/9/8
		 * @方法说明：//月度报表详情页
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@RequestMapping("/monthdetail")
	public R monthdetail(@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, false);
		if(Integer.parseInt(data.get("isNull").toString())==1)
		{
			return R.ok().put("data","").put("isNull",1);
		}
		else {

			String name = zonesService.info(zoneId).getName();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
			String dateString = formatter.format(statisticalDate);
			String uanme = name + "公共收支" + dateString + "报表";
			data.put("uname", uanme);
			data.put("year", "月");
			String content = pdfUtils.freeMarkerRender(data, "pdf-ftl/reports.ftl");
			int strStartIndex = content.indexOf("<body>");
			int strEndIndex = content.indexOf("</body>");
			if (strStartIndex < 0) {
				return R.error("无法截取目标字符串");
			}
			if (strEndIndex < 0) {
				return R.error("无法截取目标字符串");
			}
        /* 开始截取 */
			content = content.substring(strStartIndex, strEndIndex).substring("<body>".length());
			return R.ok().put("data", content).put("isNull", 0);
		}

	}

	/***
		 * @作者： zss
		 * @生成时间：16:33 2020/9/8
		 * @方法说明：//月度报表获取银行对账单
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@RequestMapping("/getFileInfoVO")
	public R getFileInfoVO(@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		return R.ok().put("data", reportsService.getFileInfoVO(statisticalDate));
	}
	/***
	 * @作者： zss
	 * @生成时间：16:29 2020/9/8
	 * @方法说明：//年度报表详情页
	 * @参数：[statisticalDate]
	 * @返回值：com.yeweihui.common.utils.R
	 */
	@RequestMapping("/yeardetail")
	public R yeardetail(@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy") Date statisticalDate) {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, true);
		if(Integer.parseInt(data.get("isNull").toString())==1)
		{
			return R.ok().put("data","").put("isNull",1);
		}
		else {
			String name = zonesService.info(zoneId).getName();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
			String dateString = formatter.format(statisticalDate);
			String uanme = name + "公共收支" + dateString + "报表";
			data.put("uname", uanme);
			data.put("year", "年");
			String content = pdfUtils.freeMarkerRender(data, "pdf-ftl/reports.ftl");
			int strStartIndex = content.indexOf("<body>");
			int strEndIndex = content.indexOf("</body>");
			if (strStartIndex < 0) {
				return R.error("无法截取目标字符串");
			}
			if (strEndIndex < 0) {
				return R.error("无法截取目标字符串");
			}
        /* 开始截取 */
			content = content.substring(strStartIndex, strEndIndex).substring("<body>".length());
			return R.ok().put("data", content).put("isNull",0);
		}
	}
	@RequestMapping("subjectDetail")
	public R subjectDetail(@RequestParam(value = "statisticalDate") String statisticalDate, @RequestParam(value = "subjectId") Integer subjectId ) {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		DateFormat format=null;
		try {
			ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam=new ReceiptsAndDisbursementsParam();
			receiptsAndDisbursementsParam.setZoneId(zoneId);
			receiptsAndDisbursementsParam.setStatus(3);
			receiptsAndDisbursementsParam.setSubjectId(subjectId);
			if(statisticalDate.length()>4)
			{
				format = new SimpleDateFormat("yyyy-MM");
				Date date = format.parse(statisticalDate);
				receiptsAndDisbursementsParam.setAccountsDate(date);
			}else {
				Integer year=Integer.parseInt(statisticalDate);
				receiptsAndDisbursementsParam.setYearDate(year);
			}
			return R.ok().put("data", reportsService.selectSubjectDetail(receiptsAndDisbursementsParam));

		}catch (Exception e)
		{
			return R.error("输入时间格式错误");

		}
	}

	/**
	 * 月度报表pdf
	 *
	 * @param response
	 * @param statisticalDate
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "monthdetailviewPdf/{statisticalDate}")
	public void monthdetailviewPdf(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, false);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "月");
		String content = pdfUtils.freeMarkerRender(data, "pdf-ftl/reports.ftl");
		//创建pdf
		pdfUtils.createPdf(content, "yeweihui-admin/target/", "reports.pdf");
		// 读取pdf并预览
		pdfUtils.readPdf(response, "yeweihui-admin/target/reports.pdf");
	}
	/**
	 * 年度报表pdf
	 *
	 * @param response
	 * @param statisticalDate
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "yeardetailviewPdf/{statisticalDate}")
	public void yeardetailviewPdf(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy") Date statisticalDate) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();

		Map data = reportsService.detail(zoneId, statisticalDate, true);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);		data.put("year", "年");
		String content = pdfUtils.freeMarkerRender(data, "pdf-ftl/reports.ftl");
		//创建pdf
		pdfUtils.createPdf(content, "yeweihui-admin/target/", "reports.pdf");
		// 读取pdf并预览
		pdfUtils.readPdf(response, "yeweihui-admin/target/reports.pdf");
	}
	/***
		 * @作者： zss
		 * @生成时间：10:13 2020/9/10
		 * @方法说明：//TODO 获取已录入信息
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@RequestMapping("/recorded")
	public R recorded(@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		return R.ok().put("data", reportsService.recorded(zoneId, statisticalDate));
	}
	/**
	 * 导出年度报表Excel
	 * @param response	响应流
	 * @param statisticalDate	年
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "yeardetailexportExcel/{statisticalDate}")
	public void yeardetailexportExcel(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy") Date statisticalDate) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();

		Map data = reportsService.detail(zoneId, statisticalDate, true);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "年");
		ExcelUtils.hetmlTableToExcel(response, pdfUtils.freeMarkerRender(data,"pdf-ftl/reports.ftl"), uanme+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}
	/**
	 * 导出月度报表Excel
	 * @param response	响应流
	 * @param statisticalDate	年
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "monthdetailexportExcel/{statisticalDate}")
	public void monthdetailexportExcel(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) throws IOException, DocumentException {
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, false);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "月");
		ExcelUtils.hetmlTableToExcel(response, pdfUtils.freeMarkerRender(data,"pdf-ftl/reports.ftl"), uanme+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}
}
