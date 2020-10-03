package com.yeweihui.modules.accounts.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.ExcelUtils;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.accounts.entity.AccountsVoucherEntity;
import com.yeweihui.modules.accounts.service.AccountsVoucherService;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.vo.query.AccountsVoucherQueryParam;
import com.yeweihui.third.pdf.PdfUtils;

import io.netty.util.internal.StringUtil;

/**
 * 凭证 - 控制器
 * 
 *
 * @author 朱晓龙
 * 2020年8月31日  下午2:40:00
 */
@RestController
@RequestMapping("accounts/voucher")
public class AccountsVoucherController {

	/**
	 * 凭证信息操作服务对象
	 */
	@Autowired
	private AccountsVoucherService accountsVoucherService;
	/**
	 * PDF工具
	 */
	@Autowired
	PdfUtils pdfUtils;
	
	/**
	 * 列表
	 *
	 * @param params 查询参数
	 * @return
	 */
    @RequestMapping("/list")
    @RequiresPermissions(value={"accounts:voucher:list","byAuditPage:accounts:voucher:list"},logical=Logical.OR)
	public R list(@RequestBody AccountsVoucherQueryParam params){
    	PageUtils page = accountsVoucherService.queryPage(params);
    	page.setList(accountsVoucherService.extendedInfo(page.getList(),BeanUtil.bean2map(params)));
		return R.ok().put("page", page);
	}
    
    /**
	 * 获取凭证信息
	 *
	 * @param id 凭证ID
	 * @return
	 */
    @RequestMapping("/info")
	public R info(@RequestParam Long id){
    	AccountsVoucherEntity accountsVoucher = accountsVoucherService.info(id);
		return R.ok().put("accountsVoucher", accountsVoucher);
	}
    
    /**
     * 添加
     *
     * @param accountsVoucher 凭证信息
     * @return
     */
    @RequestMapping("/add")
    @RequiresPermissions("accounts:voucher:add")
	public R add(@RequestBody AccountsVoucherEntity accountsVoucher){
    	accountsVoucherService.saveOrUpdate(accountsVoucher);
		return R.ok();
	}
    
    /**
     * 修改
     *
     * @param accountsVoucher 凭证信息
     * @return
     */
    @RequestMapping("/update")
    @RequiresPermissions("accounts:voucher:update")
	public R update(@RequestBody AccountsVoucherEntity accountsVoucher){
    	accountsVoucherService.saveOrUpdate(accountsVoucher);
		return R.ok();
	}
    
    /**
     * 删除
     *
     * @param id 凭证ID
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("accounts:voucher:delete")
	public R delete(@RequestParam Long id){
    	accountsVoucherService.delete(id);
		return R.ok();
	}
    
    /**
     * 获取下一位凭证记字号
     *
     * @param accountsId 账簿ID
     * @return
     */
    @RequestMapping("/nextTagNumber")
    public R getNextTagNumber(@RequestParam Long accountsId){
    	Integer nexttagNumber = accountsVoucherService.getNextTagNumber(accountsId);
    	return R.ok().put("nexttagNumber", nexttagNumber);
    }
    
    
    /**
     * 打印凭证PDF
     * @param response	响应流
     * @param ids	凭证ID
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "viewPdf/{ids}")
    @RequiresPermissions(value={"accounts:voucher:viewPdf","byAuditPage:accounts:voucher:viewPdf"},logical=Logical.OR)
	public void viewPdf(HttpServletResponse response, @PathVariable("ids") String ids) throws IOException, DocumentException {
    	LinkedList<Long> idList = new LinkedList<>();
    	if(ids==null || StringUtil.isNullOrEmpty(ids)){throw new RRException("请指定正确的凭证ID信息，多个ID使用英文“,”隔开");}
    	try{
    		for (String id : ids.split(",")) {
    			idList.add(Long.parseLong(id));
    		}
    	}catch(Exception e){
    		throw new RRException("请指定正确的凭证ID信息，多个ID使用英文“,”隔开");
    	}
        // 读取PDF文件并写入响应流
        pdfUtils.readPdf(response, accountsVoucherService.createPDFPrintFile(idList));
    }
    /**
     * 导出凭证Excel
     * @param response	响应流
     * @param ids	凭证ID
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping(value = "exportExcel/{ids}")
    @RequiresPermissions(value={"accounts:voucher:exportExcel","byAuditPage:accounts:voucher:exportExcel"},logical=Logical.OR)
	public void exportExcel(HttpServletResponse response, @PathVariable("ids") String ids) throws IOException, DocumentException {
    	LinkedList<Long> idList = new LinkedList<>();
    	if(ids==null || StringUtil.isNullOrEmpty(ids)){throw new RRException("请指定正确的凭证ID信息，多个ID使用英文“,”隔开");}
    	try{
    		for (String id : ids.split(",")) {
    			idList.add(Long.parseLong(id));
    		}
    	}catch(Exception e){
    		throw new RRException("请指定正确的凭证ID信息，多个ID使用英文“,”隔开");
    	}
        // 将heml表格数据以Excel类型返回
        ExcelUtils.hetmlTableToExcel(response, accountsVoucherService.getExportExcelContext(idList), "凭证信息_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    }
}
