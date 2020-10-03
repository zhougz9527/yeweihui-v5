package com.yeweihui.modules.accounts.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.accounts.entity.AccountsEntity;
import com.yeweihui.modules.vo.query.AccountsQueryParam;

/**
 * 账簿信息 - 数据库操作
 * 
 *
 * @author 朱晓龙
 * 2020年8月22日  下午11:28:10
 */
public interface AccountsDao extends BaseMapper<AccountsEntity> {

	/**
	 * 查询账簿信息集合
	 *
	 * @param accountsQueryParam 查询参数
	 * @return
	 */
	List<AccountsEntity> simpleList(AccountsQueryParam accountsQueryParam);
	/**
	 * 获取指定小区当前非封账状态（正在使用）的账簿信息 
	 *
	 * @param zoneId	小区ID
	 * @return
	 */
	AccountsEntity getUseAccounts(@Param("zoneId") Long zoneId);
	/**
	 * 获取指定月份已封账的账簿信息
	 *
	 * @param accountsDate 账簿所属月份，格式：yyyy-MM
	 * @return
	 */
	AccountsEntity getAccountsByMonth(@Param("zoneId") Long zoneId, @Param("accountsDate") Date accountsDate);
}
