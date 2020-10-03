package com.yeweihui.modules.accounts.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;

/**
 * 科目信息 - 服务接口
 * 
 *
 * @author 朱晓龙
 * 2020年8月17日  下午3:32:21
 */
public interface AccountsSubjectService extends IService<AccountsSubjectEntity>{

	
	/**
	 *  获取一级科目集合
	 */
	List<AccountsSubjectEntity> list();
	/**
	 *  根据ID获取科目信息
	 */
	AccountsSubjectEntity getById(Long id);
	/**
	 *  根据上级科目ID获取下级目录信息集合
	 */
	List<AccountsSubjectEntity> listByParentId(Long parentId);
	/**
	 *  根据科目ID获取详细的层级信息
	 */
	String getLevelInfo(Long id);
}
