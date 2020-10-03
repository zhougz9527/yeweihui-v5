package com.yeweihui.modules.accounts.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.modules.accounts.dao.AccountsDao;
import com.yeweihui.modules.accounts.dao.AccountsFinancialinformDao;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.accounts.service.AccountsService;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsQueryParam;
import com.yeweihui.modules.vo.query.ReceiptsAndDisbursementsParam;

import cn.hutool.core.date.DateUtil;
import io.netty.util.internal.StringUtil;

/**
 * 账簿信息 - 服务接口 - 实现方法
 * 
 *
 * @author 朱晓龙
 * 2020年8月23日  上午12:12:49
 */
@Service("accountsService")
public class AccountsServiceImpl extends ServiceImpl<AccountsDao, AccountsEntity> implements AccountsService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ZonesService zonesService;
	@Autowired
	MultimediaService multimediaService;
	
	@Autowired
	AccountsFinancialinformDao accountsFinancialinformDao;
	
	@Override
	public List<AccountsEntity> simpleList(AccountsQueryParam accountsQueryParam) {
		return this.baseMapper.simpleList(accountsQueryParam);
	}
	@Override
	public AccountsEntity getAccountsFirst(AccountsQueryParam accountsQueryParam) {
		List<AccountsEntity> resultList = simpleList(accountsQueryParam);
		if(resultList.size() > 0){
			return resultList.get(0);
		}
		return null;
	}
	@Override
	public AccountsEntity getUseAccounts() {
		return this.baseMapper.getUseAccounts(ShiroUtils.getUserEntity().getZoneId());
	}
	
	@Override
	public AccountsEntity getAccountsByMonth(Date targetDate) {
		return this.baseMapper.getAccountsByMonth(ShiroUtils.getUserEntity().getZoneId(), targetDate);
	}
	
	@Override
	public AccountsEntity newAccounts(AccountsEntity accountsEntity) {

		UserEntity userEntity = ShiroUtils.getUserEntity();
		if(userEntity.getId() == 1L){throw new RRException("系统管理员不能新启账簿");}
		if(getUseAccounts() != null){throw new RRException("当前小区已存在非封账状态的账簿信息，不允许新启账簿");}
		int checkCount = this.baseMapper.selectCount(new EntityWrapper<AccountsEntity>().eq("zone_id", userEntity.getZoneId()).eq("status", 3).ge(accountsEntity.getStartDate() != null, "end_date", accountsEntity.getStartDate()));
		if(checkCount > 0){throw new RRException("新账簿的记账日期不能小于等于当前小区已封账的封账日期");}
		AccountsEntity newAccountsEntity = new AccountsEntity();
		newAccountsEntity.setZoneId(userEntity.getZoneId());
		newAccountsEntity.setStartDate(accountsEntity.getStartDate());
		newAccountsEntity.setLastOperatingSurplus(accountsEntity.getLastOperatingSurplus());
		newAccountsEntity.setLastPledgeSurplus(accountsEntity.getLastPledgeSurplus());
		newAccountsEntity.setStatus(0);
		
		this.baseMapper.insert(newAccountsEntity);
		return newAccountsEntity;
	}
	
	@Override
	public void submitAudit(Date endDate) {

		if(endDate==null){throw new RRException("未指定封账日期");}
		AccountsEntity accounts = getUseAccounts();
		if(accounts == null){throw new RRException("当前小区无记账账簿信息");}
		if(accounts.getStatus() == 1){throw new RRException("该账簿正在审核中");}
		if(accounts.getStatus() == 3){throw new RRException("该账簿已封账");}
		if(accounts.getStatus() != 0 && accounts.getStatus() != 2){throw new RRException("该账簿状态未知");}
		if(accounts.getZoneId() != ShiroUtils.getUserEntity().getZoneId()){throw new RRException("当前登录用户与指定账簿为不同小区，无权进行审核");}
		
		String dateFormatStr = "yyyy-MM";
		Calendar startDataCalendar = Calendar.getInstance();
		startDataCalendar.setTime(accounts.getStartDate());
		Calendar endDataCalendar = Calendar.getInstance();
		endDataCalendar.setTime(endDate);
		Calendar currentDataCalendar = Calendar.getInstance();
		currentDataCalendar.setTime(new Date());
		
		
		int StartDate_Year = startDataCalendar.get(Calendar.YEAR);
		int StartDate_Month = startDataCalendar.get(Calendar.MONTH)+1;
		int StartDate_Time = (StartDate_Year*100)+StartDate_Month;
		int EndDate_Year = endDataCalendar.get(Calendar.YEAR);
		int EndDate_Month = endDataCalendar.get(Calendar.MONTH)+1;
		int EndDate_Time = (EndDate_Year*100)+EndDate_Month;
		int currentDate_Year = currentDataCalendar.get(Calendar.YEAR);
		int currentDate_Month = currentDataCalendar.get(Calendar.MONTH)+1;
		int currentDate_Time = (currentDate_Year*100)+currentDate_Month;
		
		int maxMonth = 0;
		if(currentDate_Time <= StartDate_Time){
			startDataCalendar.add(Calendar.MONTH, 1);
			throw new RRException(String.format("当前账簿只能在 %s 以后提交审核 ", DateUtil.format(startDataCalendar.getTime(), "yyyy年MM月")));
		}
		if(EndDate_Time < StartDate_Time){
			throw new RRException("封账日期不能小于记账日期 ");
		}
		if(EndDate_Year == currentDate_Year){
			if(EndDate_Month >= currentDate_Month){
				maxMonth = currentDate_Month-1;
			}
		}
		else if(EndDate_Year != StartDate_Year){
			maxMonth = 12;
		}
		
		if(maxMonth > 0){
			throw new RRException(String.format("封账日期不在有效范围之内,可选封账月份 %1$s年%2$s月 ~ %1$s年%3$s月 ", StartDate_Year,StartDate_Month,maxMonth));
		}
		
		accounts.setStatus(1);
		accounts.setEndDate(endDate);
		this.baseMapper.updateById(accounts);
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void submitAuditResult(Boolean result) {

		if(result==null){throw new RRException("审核结果不能为空");}
		AccountsEntity accounts = getUseAccounts();
		if(accounts == null){throw new RRException("当前小区无记账账簿信息");}
		if(accounts.getStatus() == 0 || accounts.getStatus() == 2){throw new RRException("请将该账簿提交审核");}
		if(accounts.getStatus() == 3){throw new RRException("该账簿已封账");}
		if(accounts.getStatus() != 1){throw new RRException("该账簿状态未知");}
		if(accounts.getZoneId() != ShiroUtils.getUserEntity().getZoneId()){throw new RRException("当前登录用户与指定账簿为不同小区，无权进行审核");}
		accounts.setStatus(result ? 3 : 2);
		accounts.setAuditor(ShiroUtils.getUserEntity().getRealname());
		this.baseMapper.updateById(accounts);
		
		//审核成功，新启账簿
		if(result){
			Calendar calendar = Calendar.getInstance();//获取当前日期
			calendar.setTime(accounts.getEndDate());
			calendar.add(Calendar.MONTH,1);
			
			ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam = new ReceiptsAndDisbursementsParam();
			receiptsAndDisbursementsParam.setZoneId(accounts.getZoneId());
			receiptsAndDisbursementsParam.setAccountsDate(accounts.getEndDate());
			receiptsAndDisbursementsParam.setType(1);
			
			//添加新的账簿
			AccountsEntity newAccounts = new AccountsEntity();
			newAccounts.setZoneId(accounts.getZoneId());
			newAccounts.setStartDate(calendar.getTime());
			String surplusStr = accountsFinancialinformDao.getSurplusByType(receiptsAndDisbursementsParam);
			Double surplus = (StringUtil.isNullOrEmpty(surplusStr)?0:Double.parseDouble(surplusStr));
			newAccounts.setLastOperatingSurplus(surplus);
			receiptsAndDisbursementsParam.setType(2);
			surplusStr = accountsFinancialinformDao.getSurplusByType(receiptsAndDisbursementsParam);
			surplus = (StringUtil.isNullOrEmpty(surplusStr)?0:Double.parseDouble(surplusStr));
			newAccounts.setLastPledgeSurplus(surplus);
			this.baseMapper.insert(newAccounts);
		}
	}
	
	@Override
	public FileInfoVO getStatementOfAccounts(Long id){
		if(id == null || id <= 0){throw new RRException("未指定账簿ID信息");}
		AccountsEntity accounts = this.baseMapper.selectById(id);
		if(accounts == null){throw new RRException("未找到账簿信息");}
		List<FileEntity> resultList = new ArrayList<FileEntity>();
		List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
                new EntityWrapper<MultimediaEntity>()
                        .eq("related_id", id)
                        .eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
                        .eq("file_type", FileTypeEnum.STATEMENTOFACCOUNT.getCode())
        );
		
        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(multimediaEntity.getName());
            fileEntity.setUrl(multimediaEntity.getUrl());
            resultList.add(fileEntity);
        }
        FileInfoVO statementOfAccountsVO = new FileInfoVO();
		statementOfAccountsVO.setId(id);
		statementOfAccountsVO.setFileInfos(resultList);
		return statementOfAccountsVO;
	}
	@Override
	public void updateStatementOfAccounts(FileInfoVO statementOfAccounts){

		AccountsEntity accounts = this.baseMapper.selectById(statementOfAccounts.getId());
		if(accounts == null){throw new RRException("未找到账簿信息");}
		if(accounts.getZoneId() != ShiroUtils.getUserEntity().getZoneId()){throw new RRException("当前登录用户与指定账簿为不同小区，无权更改对账单信息");}
		if(accounts.getStatus() == 1 || accounts.getStatus() == 3){throw new RRException(String.format("当前账簿%s，不允许更改", accounts.getStatus()==1?"正在审核中":"已封账"));}
		multimediaService.delete(
                new EntityWrapper<MultimediaEntity>()
                        .eq("related_id", statementOfAccounts.getId())
                        .eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
                        .eq("file_type", FileTypeEnum.STATEMENTOFACCOUNT.getCode())
        );
        for (FileEntity fileEntity : statementOfAccounts.getFileInfos()) {
        	MultimediaEntity multimediaEntity = new MultimediaEntity();
        	multimediaEntity.setRelatedId(statementOfAccounts.getId());
        	multimediaEntity.setRelatedType(BizTypeEnum.ACCOUNTS.getCode());
        	multimediaEntity.setFileType(FileTypeEnum.STATEMENTOFACCOUNT.getCode());
        	multimediaEntity.setName(fileEntity.getName());
        	multimediaEntity.setUrl(fileEntity.getUrl());
        	multimediaService.insert(multimediaEntity);
        }
	}
	@Override
	public boolean canAddOrUpdate(Long id) {
		AccountsEntity accounts =  this.baseMapper.selectById(id);
		return (accounts != null && (accounts.getStatus() == 0 || accounts.getStatus() == 2));
	}

	

	
}
