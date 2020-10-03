package com.yeweihui.controller.accounts;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.itextpdf.text.DocumentException;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.ExcelUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.accounts.entity.AccountsAccountEntity;
import com.yeweihui.modules.accounts.service.AccountsAccountService;
import com.yeweihui.modules.accounts.service.AccountsService;
import com.yeweihui.modules.accounts.service.ReportsService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.query.ReceiptsAndDisbursementsParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/***
	 * @作者： zss
	 * @生成时间：16:00 2020/8/21
	 * @方法说明：//报表
     *
	 */
@RestController
@RequestMapping("/api/accounts/reports")
public class ApiReportsController {

	@Autowired
	PdfUtils pdfUtils;
	@Autowired
	ReportsService reportsService;
	@Autowired
	AccountsService accountsService;
	@Autowired
	private ZonesService zonesService;
	@Autowired
	AccountsAccountService accountsAccountService;

	/**
	 * 首页收支报表月度结余
	 */
	@Login
	@RequestMapping("/blance")
	public R blance(@ApiIgnore @LoginUser UserEntity userEntity) {
		Long zoneId = userEntity.getZoneId();
		Map map = reportsService.blance(zoneId);
		return R.ok().put("data", map);
	}


	@RequestMapping("/test")
	public R test() {
		return R.ok().put("test", "test");
	}
	/***
		 * @作者： zss
		 * @生成时间：10:01 2020/9/8
		 * @方法说明：//月度报表首页
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@Login
	@RequestMapping("/monthStatistical")
	public R monthStatistical(@ApiIgnore @LoginUser UserEntity userEntity, @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		Long zoneId = userEntity.getZoneId();
		return R.ok().put("data", reportsService.monthOrYearStatistical(zoneId, statisticalDate, false));
	}


	/***
	 * @作者： zss
	 * @生成时间：10:01 2020/9/8
	 * @方法说明：//年度报表首页
	 * @参数：[statisticalDate]
	 * @返回值：com.yeweihui.common.utils.R
	 */
	@Login
	@RequestMapping("/yearStatistical")
	public R yearStatistical(@ApiIgnore @LoginUser UserEntity userEntity,@DateTimeFormat(pattern = "yyyy") Date statisticalDate) {
		Long zoneId = userEntity.getZoneId();
		return R.ok().put("data", reportsService.monthOrYearStatistical(zoneId, statisticalDate, true));
	}



	/***
		 * @作者： zss
		 * @生成时间：16:29 2020/9/8
		 * @方法说明：//月度报表详情页
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@Login
	@RequestMapping("/monthdetail")
	public R monthdetail(@ApiIgnore @LoginUser UserEntity userEntity,@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		Long zoneId = userEntity.getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, false);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "月");
		return R.ok().put("data",data);
	}



	/***
		 * @作者： zss
		 * @生成时间：16:33 2020/9/8
		 * @方法说明：//月度报表获取银行对账单
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 *//*
	@RequestMapping("/getFileInfoVO")
	public R getFileInfoVO(@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		return R.ok().put("data", reportsService.getFileInfoVO(statisticalDate));
	}*/
	/***
	 * @作者： zss
	 * @生成时间：16:29 2020/9/8
	 * @方法说明：//年度报表详情页
	 * @参数：[statisticalDate]
	 * @返回值：com.yeweihui.common.utils.R
	 */
	@Login
	@RequestMapping("/yeardetail")
	public R yeardetail(@ApiIgnore @LoginUser UserEntity userEntity,@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy") Date statisticalDate) {
		Long zoneId = userEntity.getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, true);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "年");
		return R.ok().put("data",data);
	}


	@Login
	@RequestMapping("subjectDetail")
	public R subjectDetail(@ApiIgnore @LoginUser UserEntity userEntity,@RequestParam(value = "statisticalDate") String statisticalDate, @RequestParam(value = "subjectId") Integer subjectId ) {
		Long zoneId = userEntity.getZoneId();
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


	/***
	 * @作者： zss
	 * @生成时间：9:48 2020/8/26
	 * @方法说明：//查询小区账户
	 * @参数：[]
	 * @返回值：com.yeweihui.common.utils.R
	 */
	@Login
	@RequestMapping("/search")
	public R info(@ApiIgnore @LoginUser UserEntity userEntity){
		Long zoneId = userEntity.getZoneId();
		return R.ok().put("data", accountsAccountService.selectOne(new EntityWrapper<AccountsAccountEntity>().eq("zone_id",zoneId)));
	}

	/**
	 * 月度报表pdf
	 *
	 * @param response
	 * @param statisticalDate
	 * @throws IOException
	 * @throws DocumentException
	 *//*
	@RequestMapping(value = "monthdetailviewPdf/{statisticalDate}")
	public void monthdetailviewPdf(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = userEntity.getZoneId();
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
	*//**
	 * 年度报表pdf
	 *
	 * @param response
	 * @param statisticalDate
	 * @throws IOException
	 * @throws DocumentException
	 *//*
	@RequestMapping(value = "yeardetailviewPdf/{statisticalDate}")
	public void yeardetailviewPdf(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy") Date statisticalDate) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = userEntity.getZoneId();

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
	*//***
		 * @作者： zss
		 * @生成时间：10:13 2020/9/10
		 * @方法说明：//TODO 获取已录入信息
	     * @参数：[statisticalDate]
		 * @返回值：com.yeweihui.common.utils.R
		 *//*
	@RequestMapping("/recorded")
	public R recorded(@RequestParam(value = "statisticalDate") @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) {
		Long zoneId = userEntity.getZoneId();
		return R.ok().put("data", reportsService.recorded(zoneId, statisticalDate));
	}
	*//**
	 * 导出年度报表Excel
	 * @param response	响应流
	 * @param statisticalDate	年
	 * @throws IOException
	 * @throws DocumentException
	 *//*
	@RequestMapping(value = "yeardetailexportExcel/{statisticalDate}")
	public void yeardetailexportExcel(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy") Date statisticalDate) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = userEntity.getZoneId();

		Map data = reportsService.detail(zoneId, statisticalDate, true);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "年");
		ExcelUtils.hetmlTableToExcel(response, pdfUtils.freeMarkerRender(data,"pdf-ftl/reports.ftl"), uanme+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}
	*//**
	 * 导出月度报表Excel
	 * @param response	响应流
	 * @param statisticalDate	年
	 * @throws IOException
	 * @throws DocumentException
	 *//*
	@RequestMapping(value = "monthdetailexportExcel/{statisticalDate}")
	public void monthdetailexportExcel(HttpServletResponse response, @PathVariable @DateTimeFormat(pattern = "yyyy-MM") Date statisticalDate) throws IOException, DocumentException {
		Long zoneId = userEntity.getZoneId();
		Map data = reportsService.detail(zoneId, statisticalDate, false);
		String name=zonesService.info(zoneId).getName();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月");
		String dateString = formatter.format(statisticalDate);
		String uanme=name+"公共收支"+dateString+"报表";
		data.put("uname", uanme);
		data.put("year", "月");
		ExcelUtils.hetmlTableToExcel(response, pdfUtils.freeMarkerRender(data,"pdf-ftl/reports.ftl"), uanme+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}*/
}
