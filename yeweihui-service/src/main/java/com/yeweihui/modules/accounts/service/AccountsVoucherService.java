package com.yeweihui.modules.accounts.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.service.IService;
import com.itextpdf.text.DocumentException;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.accounts.entity.AccountsVoucherEntity;
import com.yeweihui.modules.vo.query.AccountsVoucherQueryParam;

/**
 * 凭证信息 - 服务接口
 * 
 *
 * @author 朱晓龙
 * 2020年8月28日  下午5:39:29
 */
public interface AccountsVoucherService extends IService<AccountsVoucherEntity> {
	/**
	 * 获取凭证信息集合
	 *
	 * @param accountsVoucherQueryParam	查询参数
	 * @return
	 */
	List<AccountsVoucherEntity> simpleList(@Param("accountsVoucherQueryParam") AccountsVoucherQueryParam accountsVoucherQueryParam);
	/**
	 * 分页获取凭证信息集合
	 *
	 * @param accountsVoucherQueryParam	查询参数
	 * @return
	 */
	PageUtils queryPage(AccountsVoucherQueryParam accountsVoucherQueryParam);
	/**
	 * 根据账簿ID，获取凭证信息集合
	 *  
	 * @param accountsId	账簿ID
	 * @return
	 */
	List<AccountsVoucherEntity> listByAccountsId(Long accountsId);
	/**
	 * 获取详细信息
	 *
	 * @param accountsVouchers 凭证信息
	 * @return
	 */
	List<AccountsVoucherEntity> getDetail(List<AccountsVoucherEntity> accountsVouchers);
	/**
	 * 获取单条凭证的详细信息
	 *
	 * @param id 凭证ID
	 * @return
	 */
	AccountsVoucherEntity info(Long id);
	/**
	 * 更新凭证信息
	 *
	 * @param accountsVouchers 凭证信息
	 */
	void saveOrUpdate(AccountsVoucherEntity accountsVouchers);
	
	/**
	 * 删除凭证信息
	 *
	 * @param accountsVouchers 凭证信息
	 */
	void delete(Long id);
	/**
	 * 获取指定凭证下可用的下一位凭证记字号数字
	 *
	 * @param accountsid 账簿ID
	 * @return
	 */
	Integer getNextTagNumber(Long accountsId);
	/**
	 * 判断是否可以添加或修改
	 *
	 * @param id 凭证ID
	 * @return
	 */
	boolean canAddOrUpdate(Long id);

	/**
	 * 生成凭证的PDF打印文件
	 *
	 * @param id 凭证ID
	 * @return
	 */
	String createPDFPrintFile(List<Long> ids) throws IOException, DocumentException;
	/**
	 * 生成凭证的导出内容信息（Html）
	 *
	 * @param ids
	 * @return
	 */
	String getExportExcelContext(List<Long> ids);
	/**
	 * 扩展凭证信息
	 *
	 * @param vouchers 凭证信息集合
	 * @param params 其他参数
	 * @return
	 */
	List<Map<String,Object>> extendedInfo (List<AccountsVoucherEntity> vouchers, Map<String,Object> params);
}
