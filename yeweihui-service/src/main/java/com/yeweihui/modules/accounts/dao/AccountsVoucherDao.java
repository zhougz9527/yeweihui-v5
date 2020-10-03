package com.yeweihui.modules.accounts.dao;

import com.yeweihui.modules.accounts.entity.AccountsVoucherEntity;
import com.yeweihui.modules.vo.query.AccountsVoucherQueryParam;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 凭证信息 - 数据库操作
 * 
 *
 * @author 朱晓龙
 * 2020年8月31日  上午9:37:30
 */
public interface AccountsVoucherDao extends BaseMapper<AccountsVoucherEntity> {
	/**
	 * 查询凭证信息集合
	 *
	 * @param accountsVoucherQueryParam 查询参数
	 * @return
	 */
	List<AccountsVoucherEntity> simpleList(AccountsVoucherQueryParam accountsVoucherQueryParam);
	/**
	 * 查询凭证信息集合 - 分页查询
	 *
	 * @param accountsVoucherQueryParam 查询参数
	 * @return
	 */
	List<AccountsVoucherEntity> simpleList(Page page,AccountsVoucherQueryParam accountsVoucherQueryParam);
	/**
	 * 获取指定账簿下的凭证信息集合
	 *
	 * @param accountsid 账簿ID
	 * @return
	 */
	List<AccountsVoucherEntity> listByAccountsId(Long accountsid);
	/**
	 * 获取指定凭证下可用的下一位凭证记字号数字
	 *
	 * @param accountsid 账簿ID
	 * @return
	 */
	Integer getNextTagNumber(Long accountsid);
}
