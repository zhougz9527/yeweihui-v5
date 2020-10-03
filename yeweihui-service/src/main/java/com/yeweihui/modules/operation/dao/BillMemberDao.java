package com.yeweihui.modules.operation.dao;

import com.yeweihui.modules.operation.entity.BillMemberEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.BillMemberQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报销审批表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface BillMemberDao extends BaseMapper<BillMemberEntity> {

    /**
     * 根据报销id删除之前需要审批的人员
     * @param billId
     */
    void deleteByBillId(@Param("billId") Long billId);

    BillMemberEntity getByBidUid(@Param("bid") Long bid, @Param("uid") Long uid, @Param("type") Integer type);

    int getCount(BillMemberQueryParam billMemberQueryParam);

    List<BillMemberEntity> listByBillId(@Param("billId") Long billId);

    List<BillMemberEntity> simpleList(BillMemberQueryParam billMemberQueryParam);
}
