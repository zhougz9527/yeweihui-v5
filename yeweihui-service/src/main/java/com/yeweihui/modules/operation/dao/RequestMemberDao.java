package com.yeweihui.modules.operation.dao;

import com.yeweihui.modules.operation.entity.RequestMemberEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.RequestMemberQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 审批成员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface RequestMemberDao extends BaseMapper<RequestMemberEntity> {

    /**
     * 根据用章id获取审批情况列表
     * @param requestId
     * @param requestMemberStatus
     * @param requestMemberOpeType
     * @return
     */
    List<RequestMemberEntity> listByRequestId(@Param("requestId") Long requestId, @Param("requestMemberStatus") Integer requestMemberStatus,
                                              @Param("requestMemberOpeType") Integer requestMemberOpeType);

    /**
     * 根据用章id和审批用户id获取
     * @param requestId
     * @param uid
     * @return
     */
    RequestMemberEntity getByRidUid(@Param("requestId") Long requestId, @Param("uid") Long uid);

    /**
     * 获取用章参与数量
     * @param requestMemberQueryParam
     * @return
     */
    int getCount(RequestMemberQueryParam requestMemberQueryParam);
}
