package com.yeweihui.modules.accounts.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;
import com.yeweihui.modules.vo.query.AccountsSubjectQueryParam;

/**
 * 科目信息 - 数据库操作
 * 
 *
 * @author 朱晓龙
 * 2020年8月17日  下午2:48:06
 */
public interface AccountsSubjectDao extends BaseMapper<AccountsSubjectEntity> {

	/**
	 * 查询科目信息集合
	 *
	 * @param accountsSubjectQueryParam 查询参数
	 * @return
	 */
	List<AccountsSubjectEntity> simpleList(AccountsSubjectQueryParam accountsSubjectQueryParam);
	
}
