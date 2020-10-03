package com.yeweihui.modules.accounts.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;
import com.yeweihui.modules.accounts.service.AccountsService;
import com.yeweihui.modules.accounts.service.AccountsSubjectService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsQueryParam;

import io.netty.util.internal.StringUtil;

/**
 * 账簿 - 控制器
 * 
 *
 * @author 朱晓龙
 * 2020年8月23日  上午12:20:09
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("accounts/accounts")
public class AccountsController {

	/**
	 * 账簿信息操作服务对象
	 */
	@Autowired
	private AccountsService accountsService;

	/**
	 * 添加账簿，新启
	 * 
	 * @param accounts 账簿信息对象
	 */
	@RequestMapping("/add")
	@RequiresPermissions("accounts:accounts:add")
	public R add(@RequestBody AccountsEntity accounts){
		ValidatorUtils.validateEntity(accounts);
		accounts = accountsService.newAccounts(accounts);
		return R.ok().put("accounts", accounts);
	}
	
	/**
	 * 获取账簿信息
	 * 
	 * @param accounts 查询参数对象
	 */
	@RequestMapping("/info")
	public R info(@RequestBody AccountsQueryParam accountsQueryParam){
		
		AccountsEntity accounts = null;
		if(accountsQueryParam.getId() != null && accountsQueryParam.getId() > 0){
			accounts = accountsService.selectById(accountsQueryParam.getId());
		}
		else{
			accounts = accountsService.getAccountsFirst(accountsQueryParam);
		}
		return R.ok().put("accounts", accounts);
	}
	
	/**
	 * 获取当前登录用户所属小区当前正在使用的账簿信息
	 */
	@RequestMapping("/infoByUse")
	public R infoByUse(){
		
		AccountsEntity accounts = accountsService.getUseAccounts();
		return R.ok().put("accounts", accounts);
	}
	
	/**
	 * 获取当前登录用户所属小区指定月份并且已封账的账簿信息
	 * 
	 * @param accountsDate 账簿日期（月份），格式：yyyy-MM
	 */
	@RequestMapping("/infoByMonth")
	public R infoByMonth(@RequestParam(value = "accountsDate") @DateTimeFormat(pattern="yyyy-MM") Date accountsDate){
		
		AccountsEntity accounts = accountsService.getAccountsByMonth(accountsDate);
		return R.ok().put("accounts", accounts);
	}
	
	/**
	 * 提交审核
	 *
	 * @param endDate 封账日期（月份），格式：yyyy-MM
	 */
	@RequestMapping("/submitAudit")
	@RequiresPermissions("accounts:accounts:submitAudit")
	public R submitAudit(@RequestParam(value = "endDate") @DateTimeFormat(pattern="yyyy-MM") Date endDate){
		
		accountsService.submitAudit(endDate);
		return R.ok();
	}
	
	/**
	 * 提交审核结果
	 *
	 * @param result 审核结果：true(审核成功)、false(审核失败)
	 * @return
	 */
	@RequestMapping("/submitAuditResult")
	@RequiresPermissions("byAuditPage:accounts:accounts:submitAuditResult")
	public R submitAuditResult(@RequestParam(value = "result") Boolean result){
		
		accountsService.submitAuditResult(result);
		return R.ok();
	}

	/**
	 * 获取账簿关联的对账单信息集合
	 *
	 * @param id 账簿ID
	 * @return
	 */
	@RequestMapping("/getStatementOfAccounts")
	@RequiresPermissions(value={"accounts:accounts:getStatementOfAccounts","byAuditPage:accounts:accounts:getStatementOfAccounts"},logical=Logical.OR)
	public R getStatementOfAccounts(@RequestParam(value = "id") Long id){
		FileInfoVO statementOfAccountsVO = accountsService.getStatementOfAccounts(id);
		return R.ok().put("statementOfAccounts", statementOfAccountsVO);
	}
	/**
	 * 修改指定账簿的对账单信息
	 *
	 * @param id 账簿ID
	 * @param statementOfAccounts 对账单信息集合
	 * @return
	 */
	@RequestMapping("/updateStatementOfAccounts")
	@RequiresPermissions({"accounts:accounts:updateStatementOfAccounts"})
	public R updateStatementOfAccounts(@RequestBody FileInfoVO statementOfAccounts){
		accountsService.updateStatementOfAccounts(statementOfAccounts);
		return R.ok();
	}
}
