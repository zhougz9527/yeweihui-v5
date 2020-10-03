package com.yeweihui.modules.accounts.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;
import com.yeweihui.modules.accounts.service.AccountsSubjectService;

/**
 * 记账科目 - 控制器
 * 
 *
 * @author 朱晓龙
 * 2020年8月17日  下午3:46:09
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("accounts/subject")
public class AccountsSubjectController {

	/**
	 * 科目信息操作服务对象
	 */
	@Autowired
	private AccountsSubjectService accountsSubjectService;
	
	/**
	 * 获取全部一级科目信息列表
	 */
	@RequestMapping("/list")
	public R list(){
		
		List<AccountsSubjectEntity> subjectList = accountsSubjectService.list();
		return R.ok().put("subjects", subjectList);
	}
	/**
	 * 根据上级科目id获取科目信息列表
	 */
	@RequestMapping("/listByParentId")
	public R listByParentId(@RequestParam Long parentId){
		
		List<AccountsSubjectEntity> subjectList = accountsSubjectService.listByParentId(parentId);
		return R.ok().put("subjects", subjectList);
	}
	/**
	 * 根据id获取科目信息
	 */
	@RequestMapping("/info")
	public R listById(@RequestParam Long id){
		
		AccountsSubjectEntity subject = accountsSubjectService.getById(id);
		return R.ok().put("subject", subject);
	}
}
