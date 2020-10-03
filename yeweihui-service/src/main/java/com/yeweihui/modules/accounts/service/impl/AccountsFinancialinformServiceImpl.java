package com.yeweihui.modules.accounts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.vo.api.vo.FinancialinformVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.modules.accounts.dao.AccountsFinancialinformDao;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.accounts.service.AccountsFinancialinformService;
import com.yeweihui.modules.accounts.service.AccountsSubjectService;
import com.yeweihui.modules.accounts.service.AccountsVoucherService;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsFinancialinformQueryParam;

/**
 * 财务收支信息 - 服务接口 - 实现方法
 * 
 *
 * @author 朱晓龙
 * 2020年8月28日  下午2:36:29
 */
@Service("accountsFinancialinformService")
public class AccountsFinancialinformServiceImpl extends ServiceImpl<AccountsFinancialinformDao, AccountsFinancialinformEntity> implements AccountsFinancialinformService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AccountsSubjectService accountsSubjectService;
	@Autowired
	AccountsVoucherService accountsVoucherService;
	@Autowired
	MultimediaService multimediaService;
	
	@Override
	public List<AccountsFinancialinformEntity> simpleList(AccountsFinancialinformQueryParam accountsFinancialinformQueryParam) {
		List<AccountsFinancialinformEntity> accountsFinancialinforms = this.baseMapper.simpleList(accountsFinancialinformQueryParam);
		return accountsFinancialinforms;
	}

	@Override
	public List<AccountsFinancialinformEntity> listByVoucherId(Long voucherId) {
		AccountsFinancialinformQueryParam accountsFinancialinformQueryParam = new AccountsFinancialinformQueryParam();
		accountsFinancialinformQueryParam.setVoucherId(voucherId);
		return simpleList(accountsFinancialinformQueryParam);
	}
	
	@Override
	public List<AccountsFinancialinformEntity> getDetail(List<AccountsFinancialinformEntity> accountsFinancialinforms){
		for(int i=0,l=accountsFinancialinforms.size(); i<l; i++){
			
			accountsFinancialinforms.get(i).setAccountsSubject(accountsSubjectService.selectById(accountsFinancialinforms.get(i).getSubjectId()));
			
			FileInfoVO accessory = new FileInfoVO();
			accessory.setId(accountsFinancialinforms.get(i).getId());
			accessory.setFileInfos(new ArrayList<FileEntity>());
			List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
					new EntityWrapper<MultimediaEntity>()
							.eq("related_id", accessory.getId())
							.eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
							.eq("file_type", FileTypeEnum.ACCESSORY.getCode())
			);
			for (MultimediaEntity multimediaEntity : multimediaEntityList) {
				FileEntity fileEntity = new FileEntity();
				fileEntity.setUid(multimediaEntity.getId().toString());
				fileEntity.setName(multimediaEntity.getName());
				fileEntity.setUrl(multimediaEntity.getUrl());
				accessory.getFileInfos().add(fileEntity);
			}
			accountsFinancialinforms.get(i).setAccessory(accessory);
		}
		return accountsFinancialinforms;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long voucherId, List<AccountsFinancialinformEntity> accountsFinancialinforms) {
		for (AccountsFinancialinformEntity accountsFinancialinform : accountsFinancialinforms) {
			
			accountsFinancialinform.setVoucherId(voucherId);
			this.insertOrUpdate(accountsFinancialinform);

			multimediaService.delete(
	                new EntityWrapper<MultimediaEntity>()
	                        .eq("related_id", accountsFinancialinform.getId())
	                        .eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
	                        .eq("file_type", FileTypeEnum.ACCESSORY.getCode())
	        );
			if(accountsFinancialinform.getAccessory().getFileInfos()==null){return;}
	        for (FileEntity fileEntity : accountsFinancialinform.getAccessory().getFileInfos()) {
	        	MultimediaEntity multimediaEntity = new MultimediaEntity();
	        	multimediaEntity.setRelatedId(accountsFinancialinform.getId());
	        	multimediaEntity.setRelatedType(BizTypeEnum.ACCOUNTS.getCode());
	        	multimediaEntity.setFileType(FileTypeEnum.ACCESSORY.getCode());
	        	multimediaEntity.setName(fileEntity.getName());
	        	multimediaEntity.setUrl(fileEntity.getUrl());
	        	multimediaService.insert(multimediaEntity);
	        }
		}
	}

	@Override
	public void addAccessory(FileInfoVO accessory) {
		if(accessory.getId()<=0){throw new RRException("请指定正确的财务收支信息ID");}
		if(accessory.getFileInfos()==null){throw new RRException("附件信息不能为空");}
		if(!canAddOrUpdate(accessory.getId())){throw new RRException("所属账簿状态不允许再对财务收支附件进行添加或删除");}
        for (FileEntity fileEntity : accessory.getFileInfos()) {
        	MultimediaEntity multimediaEntity = new MultimediaEntity();
        	multimediaEntity.setRelatedId(accessory.getId());
        	multimediaEntity.setRelatedType(BizTypeEnum.ACCOUNTS.getCode());
        	multimediaEntity.setFileType(FileTypeEnum.ACCESSORY.getCode());
        	multimediaEntity.setName(fileEntity.getName());
        	multimediaEntity.setUrl(fileEntity.getUrl());
        	multimediaService.insert(multimediaEntity);
        }
		
	}

	@Override
	public void deleteAccessory(FileInfoVO accessory) {
		if(accessory.getId()<=0){throw new RRException("请指定正确的财务收支信息ID");}
		if(accessory.getFileInfos()==null){throw new RRException("附件信息不能为空");}
		if(!canAddOrUpdate(accessory.getId())){throw new RRException("所属账簿状态不允许再对财务收支附件进行添加或删除");}
		List<String> uids = accessory.getFileInfos().stream().map(p -> p.getUid()).collect(Collectors.toList());
		multimediaService.delete(
                new EntityWrapper<MultimediaEntity>()
                		.in("id", uids)
                        .eq("related_id", accessory.getId())
                        .eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
                        .eq("file_type", FileTypeEnum.ACCESSORY.getCode()
                        )
        );
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByVoucherId(Long voucherId){
		
		if(!accountsVoucherService.canAddOrUpdate(voucherId)){throw new RRException("所属账簿状态不允许删除财务收支信息");}
		List<AccountsFinancialinformEntity> Financialinforms = listByVoucherId(voucherId);
		for (AccountsFinancialinformEntity accountsFinancialinformEntity : Financialinforms) {
			multimediaService.delete(
	                new EntityWrapper<MultimediaEntity>()
	                        .eq("related_id", accountsFinancialinformEntity.getId())
	                        .eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
	                        .eq("file_type", FileTypeEnum.ACCESSORY.getCode()
	                        )
	        );
		}
		this.baseMapper.delete(new EntityWrapper<AccountsFinancialinformEntity>().eq(voucherId != null,"voucher_id", voucherId));
	}
	
	@Override
	public boolean canAddOrUpdate(Long id){
		
		AccountsFinancialinformEntity accountsFinancialinform =  this.baseMapper.selectById(id);
		if(accountsFinancialinform != null){
			return accountsVoucherService.canAddOrUpdate(accountsFinancialinform.getVoucherId());
		}
		return false;
	}

	@Override
	public List<Long> getVoucherIdsBySubjectIds(List<Long> subjectIds) {
		return this.baseMapper.getVoucherIdsBySubjectIds(subjectIds);
	}

	@Override
	@ZoneFilter
	public PageUtils queryPage(Map<String, Object> params) {
		Page page = new Query(params).getPage();
		List<FinancialinformVO> list = this.baseMapper.selectPageEn(page, params);
		page.setRecords(this.getFinancialinformVODetail(list));
		return new PageUtils(page);
	}

	public List<FinancialinformVO> getFinancialinformVODetail(List<FinancialinformVO> accountsFinancialinforms){
		for(int i=0,l=accountsFinancialinforms.size(); i<l; i++){

			accountsFinancialinforms.get(i).setAccountsSubject(accountsSubjectService.selectById(accountsFinancialinforms.get(i).getSubjectId()));

			FileInfoVO accessory = new FileInfoVO();
			accessory.setId(accountsFinancialinforms.get(i).getId());
			accessory.setFileInfos(new ArrayList<FileEntity>());
			List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
					new EntityWrapper<MultimediaEntity>()
							.eq("related_id", accessory.getId())
							.eq("related_type", BizTypeEnum.ACCOUNTS.getCode())
							.eq("file_type", FileTypeEnum.ACCESSORY.getCode())
			);
			for (MultimediaEntity multimediaEntity : multimediaEntityList) {
				FileEntity fileEntity = new FileEntity();
				fileEntity.setUid(multimediaEntity.getId().toString());
				fileEntity.setName(multimediaEntity.getName());
				fileEntity.setUrl(multimediaEntity.getUrl());
				accessory.getFileInfos().add(fileEntity);
			}
			accountsFinancialinforms.get(i).setAccessory(accessory);
		}
		return accountsFinancialinforms;
	}
}
