package com.yeweihui.modules.accounts.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.FinancialinformVO;
import com.yeweihui.modules.vo.api.vo.SubjectVO;
import com.yeweihui.modules.vo.query.AccountsFinancialinformQueryParam;
import com.yeweihui.modules.vo.query.ReceiptsAndDisbursementsParam;

import java.util.List;
import java.util.Map;


public interface AccountsFinancialinformDao extends BaseMapper<AccountsFinancialinformEntity> {

    /**
     * 查询收支信息集合
     *
     * @param receiptsAndDisbursementsParam 查询参数
     * @return
     */
    String getReceiptsAndDisbursements(ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam);

    /**
     * 查询结余信息集合
     *
     * @param receiptsAndDisbursementsParam 查询参数
     * @return
     */
    String getSurplusByType(ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam);


    /**
     * 按科目分组统计收支信息集合
     *
     * @param receiptsAndDisbursementsParam 查询参数
     * @return
     */

    List<SubjectVO> getReceiptsAndDisbursementsBYSubject(ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam);

    /**
     * 按科目查询
     *
     * @param receiptsAndDisbursementsParam 查询参数
     * @return
     */
    List<FinancialinformVO> selectSubjectDetail(ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam);
    /**
     * 查询财务信息集合
     *
     * @param accountsSubjectQueryParam 查询参数
     * @return
     */
    List<AccountsFinancialinformEntity> simpleList(AccountsFinancialinformQueryParam accountsSubjectQueryParam);
    /**
     * 根据财务收支的科目ID获取凭证ID集合
     *
     * @param ids 科目ID
     * @return
     */
    List<Long> getVoucherIdsBySubjectIds(List<Long> subjectIds);
    /***
    	 * @作者： zss
    	 * @生成时间：19:33 2020/9/3
    	 * @方法说明：//分页查询收支
         * @参数：
    	 * @返回值：
    	 */
    List<FinancialinformVO> selectPageEn(Page page, Map<String, Object> params);
}
