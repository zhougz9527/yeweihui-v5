package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.BillMemberEntity;
import com.yeweihui.modules.vo.query.BillMemberQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 报销审批表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface BillMemberService extends IService<BillMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BillMemberEntity> simpleList(BillMemberQueryParam billMemberQueryParam);

    /**
     * 根据报销id删除之前需要审批的人员
     * @param billId
     */
    void deleteByBillId(Long billId);

    BillMemberEntity getByBidUid(Long bid, Long uid, Integer type);

    int getCount(BillMemberQueryParam billMemberQueryParam);

    /**
     * 审核人查询
     * @param id
     * @return
     */
    List<BillMemberEntity> listByBillId(Long id);
}

