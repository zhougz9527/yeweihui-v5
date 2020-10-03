package com.yeweihui.modules.accounts.service.impl;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.itextpdf.text.DocumentException;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.accounts.dao.AccountsVoucherDao;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.accounts.entity.AccountsVoucherEntity;
import com.yeweihui.modules.accounts.service.AccountsFinancialinformService;
import com.yeweihui.modules.accounts.service.AccountsService;
import com.yeweihui.modules.accounts.service.AccountsSubjectService;
import com.yeweihui.modules.accounts.service.AccountsVoucherService;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.sys.entity.SysDictEntity;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsVoucherQueryParam;
import com.yeweihui.third.pdf.PdfUtils;

import io.netty.util.internal.StringUtil;

/**
 * 凭证信息 - 服务接口 - 实现方法
 * 
 *
 * @author 朱晓龙
 * 2020年8月28日  下午5:50:34
 */
@Service("accountsVoucherService")
public class AccountsVoucherServiceImpl extends ServiceImpl<AccountsVoucherDao, AccountsVoucherEntity> implements AccountsVoucherService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Autowired
	AccountsService accountsService;
	@Autowired
	AccountsFinancialinformService accountsFinancialinformService;
	@Autowired
	AccountsSubjectService accountsSubjectService;
	@Autowired
	PdfUtils pdfUtils;
	@Autowired
	ZonesService zonesService;
	
	@Override
	public List<AccountsVoucherEntity> simpleList(AccountsVoucherQueryParam accountsVoucherQueryParam) {
		return this.baseMapper.simpleList(accountsVoucherQueryParam);
	}
	
	@Override
	public PageUtils queryPage(AccountsVoucherQueryParam accountsVoucherQueryParam) {

		Map params = BeanUtil.bean2map(accountsVoucherQueryParam);
		Page<AccountsVoucherEntity> page = new Query<AccountsVoucherEntity>(params).getPage();
		if(accountsVoucherQueryParam.getAccountsId() > 0){
			AccountsEntity accounts = accountsService.selectById(accountsVoucherQueryParam.getAccountsId());
			if(accounts==null || accounts.getStatus() >= 3 || accounts.getZoneId() != ShiroUtils.getUserEntity().getZoneId()){
				return new PageUtils(page);
			}
		}
		else{
			throw new RRException("账簿ID不能为空");
		}

		List<AccountsVoucherEntity> list = this.baseMapper.simpleList(page, accountsVoucherQueryParam);
        page.setRecords(getDetail(list));
        return new PageUtils(page);
	}
	
	
	@Override
	public List<AccountsVoucherEntity> listByAccountsId(Long accountsId) {
		return this.baseMapper.listByAccountsId(accountsId);
	}

	@Override
	public List<AccountsVoucherEntity> getDetail(List<AccountsVoucherEntity> accountsVouchers) {
		for(int i=0,l=accountsVouchers.size(); i<l; i++){
			accountsVouchers.get(i).setAccountsFinancialinforms(accountsFinancialinformService.getDetail(accountsFinancialinformService.listByVoucherId(accountsVouchers.get(i).getId())));
		}
		return accountsVouchers;
	}
	
	@Override
	public AccountsVoucherEntity info(Long id) {

		AccountsVoucherEntity accountsVoucher = selectById(id);
		if(accountsVoucher != null){
			accountsVoucher.setAccountsFinancialinforms(accountsFinancialinformService.getDetail(accountsFinancialinformService.listByVoucherId(accountsVoucher.getId())));
		}
		return accountsVoucher;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(AccountsVoucherEntity accountsVoucher) {
		
		if(accountsVoucher.getAccountsId() <= 0){throw new RRException("凭证绑定的账簿ID不正确");}
		if(!accountsService.canAddOrUpdate(accountsVoucher.getAccountsId())){throw new RRException("当前账簿状态不允许添加或编辑凭证");}
		if(accountsVoucher.getAccountsFinancialinforms().size() > 6){throw new RRException("一条凭证最多只能录入6条财务信息");}

		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar currentDateCalendar = Calendar.getInstance();
		currentDateCalendar.setTime(new Date());
		/*try {
			currentDateCalendar.setTime(SimpleDateFormat.parse(SimpleDateFormat.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		int year = currentDateCalendar.get(Calendar.YEAR);
		int month = currentDateCalendar.get(Calendar.MONTH)+1;
		int maxDay = currentDateCalendar.getActualMaximum(Calendar.DATE);

		Date startDate = null;
		Date endDate = null;
		try {
			startDate = SimpleDateFormat.parse(String.format("%s-%s-%s", year,month,1));
			endDate = SimpleDateFormat.parse(String.format("%s-%s-%s", year,month,maxDay));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(accountsVoucher.getDate().getTime() < startDate.getTime() || accountsVoucher.getDate().getTime() > endDate.getTime()){throw new RRException("只允许添加或编辑当月的凭证");}
		
		if(accountsVoucher.getId() <= 0){
			accountsVoucher.setTagNumber(getNextTagNumber(accountsVoucher.getAccountsId()));
		}else{
			List<AccountsFinancialinformEntity> accountsFinancialinforms = accountsFinancialinformService.getDetail(accountsFinancialinformService.listByVoucherId(accountsVoucher.getId()));
			List<Long> AFIds = accountsVoucher.getAccountsFinancialinforms().stream().filter(entry->entry.getId()!=null&&entry.getId()>0).map(entry->entry.getId()).collect(Collectors.toList());
			for(int i=0;i<accountsFinancialinforms.size();i++){
				if(AFIds.indexOf(accountsFinancialinforms.get(i).getId())<0){
					accountsFinancialinformService.deleteAccessory(accountsFinancialinforms.get(i).getAccessory());
					accountsFinancialinformService.deleteById(accountsFinancialinforms.get(i).getId());
				}
			}
		}
		this.insertOrUpdate(accountsVoucher);
		if(accountsVoucher.getAccountsFinancialinforms() != null && accountsVoucher.getAccountsFinancialinforms().size() > 0){
			accountsFinancialinformService.saveOrUpdate(accountsVoucher.getId(), accountsVoucher.getAccountsFinancialinforms());
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) {
		if(id!=null){
			if(!canAddOrUpdate(id)){throw new RRException("不允许删除当前凭证");}
			AccountsVoucherEntity vocher = this.baseMapper.selectById(id);
			accountsFinancialinformService.deleteByVoucherId(id);
			this.baseMapper.deleteById(id);
		}
	}
	
	@Override
	public Integer getNextTagNumber(Long accountsId) {
		if(accountsId <= 0){throw new RRException("请指定正确的账簿ID");}
		if(accountsService.selectById(accountsId) == null){throw new RRException("账簿不存在");}
		Integer nextTagNumber = this.baseMapper.getNextTagNumber(accountsId);
		return (nextTagNumber==null)?1:nextTagNumber;
	}
	
	@Override
	public boolean canAddOrUpdate(Long id) {
		AccountsVoucherEntity accountsVoucher =  this.baseMapper.selectById(id);
		if(accountsVoucher != null){
			return accountsService.canAddOrUpdate(accountsVoucher.getAccountsId());
		}
		return false;
	}

	@Override
	public String createPDFPrintFile(List<Long> ids) throws IOException, DocumentException {
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> voucherInfos = new LinkedList<Map<String,Object>>();
		for (Long id : ids) {
			Map<String,Object> data = new HashMap<String,Object>();
			try{
				if(id == null || id <= 0){throw new RRException("请指定正确的凭证ID");}
				// 需要填充的数据
				AccountsVoucherEntity voucher  = this.info(id);
    			if(voucher == null){throw new RRException("未找到匹配的凭证信息,凭证ID为："+id);};
    		
    			AccountsEntity accounts = accountsService.selectById(voucher.getAccountsId());

    			data.put("accounts", accounts);
    			data.put("voucher", voucher);
    		
    			for (AccountsFinancialinformEntity accountsFinancialinform : voucher.getAccountsFinancialinforms()) {
    				accountsFinancialinform.getAccountsSubject().setLevelInfo(accountsSubjectService.getLevelInfo(accountsFinancialinform.getAccountsSubject().getId()));
				}
    		
    			data.put("zoneName", zonesService.selectById(accounts.getZoneId()).getName());
    		
    			int accessoryCount = 0;
    			for (AccountsFinancialinformEntity accountsFinancialinform : voucher.getAccountsFinancialinforms()) {
    			accessoryCount += accountsFinancialinform.getAccessory().getFileInfos().size();
				}
    			data.put("accessoryCount", accessoryCount);
    			voucherInfos.add(data);
			}catch(RRException e){
				throw e;
			}
		}
		resultMap.put("voucherInfos", voucherInfos);
        String content = pdfUtils.freeMarkerRender(resultMap,"pdf-ftl/voucher_print.ftl");
        String tempSavePath = "yeweihui-admin/target/";
        String tempSaveName = "voucher_print.pdf";
        // 创建PDF
		pdfUtils.createPdf(content, tempSavePath, tempSaveName);
		return tempSavePath+tempSaveName;
	}
	
	@Override
	public String getExportExcelContext(List<Long> ids)
	{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> voucherInfos = new LinkedList<Map<String,Object>>();
		for (Long id : ids) {
			Map<String,Object> data = new HashMap<String,Object>();
			try{
				if(id == null || id <= 0){throw new RRException("请指定正确的凭证ID");}
				// 需要填充的数据
				AccountsVoucherEntity voucher  = this.info(id);
    			if(voucher == null){throw new RRException("未找到匹配的凭证信息,凭证ID为："+id);};
    		
    			AccountsEntity accounts = accountsService.selectById(voucher.getAccountsId());

    			data.put("accounts", accounts);
    			data.put("voucher", voucher);
    		
    			for (AccountsFinancialinformEntity accountsFinancialinform : voucher.getAccountsFinancialinforms()) {
    				accountsFinancialinform.getAccountsSubject().setLevelInfo(accountsSubjectService.getLevelInfo(accountsFinancialinform.getAccountsSubject().getId()));
				}

    			voucherInfos.add(data);
			}catch(RRException e){
				throw e;
			}
		}
		resultMap.put("voucherInfos", voucherInfos);
        return pdfUtils.freeMarkerRender(resultMap,"excel-ftl/voucher_export.ftl");
	}
	
	@Override
 	public List<Map<String,Object>> extendedInfo(List<AccountsVoucherEntity> vouchers, Map<String,Object> params){
		List<Map<String,Object>> lineInfos = new LinkedList<>();
		AccountsEntity accounts = null;
		if(params.containsKey("accountsId") && !StringUtil.isNullOrEmpty(params.get("accountsId").toString())){
			accounts = accountsService.selectById(params.get("accountsId").toString());
		}
		for (AccountsVoucherEntity voucher : vouchers) {
			Map<String,Object> voucherInfoMap = BeanUtil.bean2map(voucher);
			if(accounts != null){
				voucherInfoMap.put("auditor", accounts.getAuditor());
			}
			lineInfos.add(voucherInfoMap);
		}
		return lineInfos;
	}



	
}
