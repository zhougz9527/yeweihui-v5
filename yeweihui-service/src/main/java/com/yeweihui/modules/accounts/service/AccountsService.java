package com.yeweihui.modules.accounts.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsQueryParam;

/**
 * 账簿信息 - 服务接口
 * 
 *
 * @author 朱晓龙
 * 2020年8月23日  上午12:08:48
 */
public interface AccountsService extends IService<AccountsEntity>{

	
	/**
	 *  获取账簿信息集合
	 */
	List<AccountsEntity> simpleList(@Param("accountsQueryParam") AccountsQueryParam accountsQueryParam);
	/**
	 * 获取第一条匹配的账簿信息
	 *
	 * @param accountsQueryParam 查询参数对象
	 * @return
	 */
	AccountsEntity getAccountsFirst(AccountsQueryParam accountsQueryParam);
	/**
	 * 获取登录用户所属小区当前非封账状态（正在使用）的账簿信息
	 *
	 * @return
	 */
	AccountsEntity getUseAccounts();
	/**
	 * 获取登录用户所属小区指定月份已封账的账簿信息
	 *
	 * @param zoneId 小区ID
	 * @param accountsDate 账簿所属月份，格式：yyyy-MM
	 * @return
	 */
	AccountsEntity getAccountsByMonth(Date accountsDate);
	/**
	 * 新启账簿
	 *
	 * @param accountsEntity 添加的账簿信息
	 * @return
	 */
	AccountsEntity newAccounts(AccountsEntity accountsEntity);
	
	/**
	 * 将当前登录用户所属小区正在记账的账簿，进行提交审核
	 *
	 * @param endDate 封账日期，格式：yyyy-MM
	 */
	void submitAudit(Date endDate);
	/**
	 * 将当前登录用户所属小区正在审核中的账簿，进行审核，审核成功后自动开启新的账簿
	 *
	 * @param result 审核结果
	 */
	void submitAuditResult(Boolean result);

	/**
	 * 获取账簿的对账单信息
	 *
	 * @param id 账簿ID
	 * @return
	 */
	FileInfoVO getStatementOfAccounts(Long id);
	/**
	 * 更新账簿的对账单信息
	 *
	 * @param statementOfAccounts 对账单信息
	 * @return
	 */
	void updateStatementOfAccounts(FileInfoVO statementOfAccounts);
	
	/**
	 * 判断是否可以添加或修改
	 *
	 * @param id 账簿ID
	 * @return
	 */
	boolean canAddOrUpdate(Long id);
}
