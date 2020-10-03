package com.yeweihui.modules.accounts.service;

import java.util.List;
import java.util.Map;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.vo.api.vo.FinancialinformVO;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;
import com.yeweihui.modules.vo.query.AccountsFinancialinformQueryParam;

/**
 * 财务收支信息 - 服务接口
 * 
 *
 * @author 朱晓龙
 * 2020年8月28日  下午2:12:41
 */
public interface AccountsFinancialinformService extends IService<AccountsFinancialinformEntity> {
	/**
	 * 获取财务收支信息集合
	 *
	 * @param accountsFinancialinformQueryParam	查询参数
	 * @return
	 */
	List<AccountsFinancialinformEntity> simpleList(@Param("accountsFinancialinformQueryParam") AccountsFinancialinformQueryParam accountsFinancialinformQueryParam);

	/**
	 * 根据凭证ID，获取财务收支信息集合
	 *  
	 * @param voucherId	凭证ID
	 * @return
	 */
	List<AccountsFinancialinformEntity> listByVoucherId(Long voucherId);
	/**
	 * 获取详细信息
	 *
	 * @param accountsFinancialinforms 财务收支信息
	 * @return
	 */
	List<AccountsFinancialinformEntity> getDetail(List<AccountsFinancialinformEntity> accountsFinancialinforms);
	/**
	 * 更新财务信息
	 *
	 * @param accountsFinancialinforms 财务信息
	 */
	void saveOrUpdate(Long voucherId, List<AccountsFinancialinformEntity> accountsFinancialinforms);
	/**
	 * 添加附件
	 *
	 * @param accessory 附件信息
	 */
	void addAccessory(FileInfoVO accessory);
	/**
	 * 根据凭证ID删除财务收支信息
	 *
	 * @param voucherId 凭证ID
	 */
	void deleteByVoucherId(Long voucherId);
	/**
	 * 删除附件
	 *
	 * @param accessory 附件信息
	 */
	void deleteAccessory(FileInfoVO accessory);
	/**
	 * 判断是否可以添加或修改
	 *
	 * @param id 财务信息ID
	 * @return
	 */
	boolean canAddOrUpdate(Long id);
	/**
	 * 根据财务收支的科目ID获取凭证ID集合
	 *
	 * @param ids 科目ID
	 * @return
	 */
	List<Long> getVoucherIdsBySubjectIds(List<Long> subjectIds);

	PageUtils queryPage(Map<String, Object> params);

	List<FinancialinformVO> getFinancialinformVODetail(List<FinancialinformVO> accountsFinancialinforms);

	}
