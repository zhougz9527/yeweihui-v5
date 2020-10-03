package com.yeweihui.modules.accounts.controller;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.ExcelUtils;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.vo.query.AccountsFinancialinformQueryParam;
import com.yeweihui.modules.vo.query.AccountsVoucherQueryParam;
import com.yeweihui.third.pdf.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.yeweihui.common.utils.R;
import com.yeweihui.modules.accounts.service.AccountsFinancialinformService;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 财务收支信息 - 控制器
 * 
 *
 * @author 朱晓龙
 * 2020年8月31日  下午3:29:44
 */
@RestController
@RequestMapping("accounts/financialinform")
public class AccountsFinancialinformController {
	/**
	 * 财务收支信息操作服务对象
	 */
	@Autowired
	private AccountsFinancialinformService accountsFinancialinformService;

	@Autowired
	PdfUtils pdfUtils;
	/**
     * 添加
     *
     * @param accessory 附件文件信息
     * @return
     */
    @RequestMapping("/addAccessory")
	public R addAccessory(@RequestBody FileInfoVO accessory){
    	accountsFinancialinformService.addAccessory(accessory);
		return R.ok();
	}
    
    /**
     * 删除
     *
     * @param accessory 附件文件信息
     * @return
     */
    @RequestMapping("/deleteAccessory")
	public R deleteAccessory(@RequestBody FileInfoVO accessory){
    	accountsFinancialinformService.deleteAccessory(accessory);
		return R.ok();
	}

	/***
		 * @作者： zss
		 * @生成时间：16:24 2020/9/8
		 * @方法说明：//收支详情分页查询
	     * @参数：[params]
		 * @返回值：com.yeweihui.common.utils.R
		 */
	@RequestMapping("/list")
	public R list(AccountsFinancialinformQueryParam params){
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		params.setZoneId(zoneId);
		PageUtils page = accountsFinancialinformService.queryPage(BeanUtil.bean2map(params));
		return R.ok().put("page", page);
	}
	/**
	 * 收支详情pdf
	 *
	 * @param response
	 * @param params
	 * @throws IOException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "/financialinformviewPdf")
	public void financialinformviewPdf(HttpServletResponse response, AccountsFinancialinformQueryParam params) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		params.setZoneId(zoneId);
		PageUtils page = accountsFinancialinformService.queryPage(BeanUtil.bean2map(params));
		Map data = new HashMap();
		data.put("flist",page.getList());
		data.put("uname", "收支详情");
		String content = pdfUtils.freeMarkerRender(data, "pdf-ftl/rdDetail.ftl");
		//创建pdf
		pdfUtils.createPdf(content, "yeweihui-admin/target/", "rdDetail.pdf");
		// 读取pdf并预览
		pdfUtils.readPdf(response, "yeweihui-admin/target/rdDetail.pdf");
	}
	 /**
     * 导出收支详情Excel
     * @param response	响应流
     * @param params	分页和查询信息
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "exportExcel")
    public void exportExcel(HttpServletResponse response, AccountsFinancialinformQueryParam params) throws IOException, DocumentException {
		//需要填充的数据
		Long zoneId = ShiroUtils.getUserEntity().getZoneId();
		params.setZoneId(zoneId);
		PageUtils page = accountsFinancialinformService.queryPage(BeanUtil.bean2map(params));
		Map<String,Object> data = new HashMap<String, Object>();

		data.put("flist",page.getList());
		data.put("uname", "收支详情");
		ExcelUtils.hetmlTableToExcel(response, pdfUtils.freeMarkerRender(data,"pdf-ftl/rdDetail.ftl"), "收支详情_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

	}
}
