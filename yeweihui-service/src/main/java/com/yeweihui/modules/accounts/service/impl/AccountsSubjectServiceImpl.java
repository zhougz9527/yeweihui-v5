package com.yeweihui.modules.accounts.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.accounts.dao.AccountsSubjectDao;
import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;
import com.yeweihui.modules.accounts.service.AccountsSubjectService;

/**
 * 科目信息 - 服务接口 - 实现方法
 * 
 *
 * @author 朱晓龙
 * 2020年8月17日  下午3:37:25
 */
@Service("accountsSubjectService")
public class AccountsSubjectServiceImpl extends ServiceImpl<AccountsSubjectDao, AccountsSubjectEntity> implements AccountsSubjectService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<AccountsSubjectEntity> list() {
		return listByParentId(Long.decode("0"));
	}

	@Override
	public AccountsSubjectEntity getById(Long id) {
		AccountsSubjectEntity accountsSubjectEntity = this.baseMapper.selectById(id);
		if(accountsSubjectEntity!=null){
			accountsSubjectEntity.setChildren(listByParentId(accountsSubjectEntity.getId()));
		}
		return accountsSubjectEntity;
	}
	
	@Override
	public List<AccountsSubjectEntity> listByParentId(Long parentId){
		Map<Long,AccountsSubjectEntity> allSubjects = loadAllSubject();
		List<AccountsSubjectEntity> children = new ArrayList<AccountsSubjectEntity>();
		if(allSubjects.containsKey(parentId)){
			children = allSubjects.get(parentId).getChildren();
		}
		return children;
	}

	/**
	 * 获取全部科目信息，整理上下级科目关系
	 */
	private Map<Long,AccountsSubjectEntity> loadAllSubject(){
		Wrapper<AccountsSubjectEntity> wrapper = new EntityWrapper<>();
		List<AccountsSubjectEntity> allSubject = this.baseMapper.selectList(wrapper);
		AccountsSubjectEntity root = new AccountsSubjectEntity();
		root.setChildren(new ArrayList<AccountsSubjectEntity>());
		Map<Long,AccountsSubjectEntity> subjectsMap = new HashMap<Long,AccountsSubjectEntity>();
		subjectsMap.put(Long.decode("0"), root);
		for(int i=0;i<allSubject.size();i++){
			subjectsMap.put(allSubject.get(i).getId(), allSubject.get(i));
		}
		for(int i=0;i<allSubject.size();i++){
			if(subjectsMap.containsKey(allSubject.get(i).getParentId())){
				if(subjectsMap.get(allSubject.get(i).getParentId()).getChildren() == null){
					subjectsMap.get(allSubject.get(i).getParentId()).setChildren(new ArrayList<AccountsSubjectEntity>());
				}
				subjectsMap.get(allSubject.get(i).getParentId()).getChildren().add(allSubject.get(i));
			}
		}
		return subjectsMap;
	}
	
	
	@Override
	public String getLevelInfo(Long id) {
		
		String idInfo = "";
		String nameInfo = "";
		
		AccountsSubjectEntity subject = this.baseMapper.selectById(id);
		if(subject!=null){
			AccountsSubjectEntity checkSubject = subject;
			while(checkSubject != null){
				if(idInfo.length()>0){idInfo = "."+idInfo;}
				if(nameInfo.length()>0){nameInfo = "_"+nameInfo;}
				idInfo = checkSubject.getId()+idInfo;
				nameInfo = checkSubject.getName()+nameInfo;
				
				if(checkSubject.getParentId() > 0){
					checkSubject = this.baseMapper.selectById(checkSubject.getParentId());
				}
				else{
					checkSubject = null;
				}
			}
		}

		return idInfo+"-"+nameInfo;
	}

}
