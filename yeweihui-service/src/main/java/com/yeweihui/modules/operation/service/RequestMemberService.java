package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.RequestMemberEntity;
import com.yeweihui.modules.vo.query.RequestMemberQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 审批成员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface RequestMemberService extends IService<RequestMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据用章id获取审批情况列表
     * @param requestId
     * @param requestMemberStatus
     * @param requestMemberOpeType
     * @return
     */
    List<RequestMemberEntity> listByRequestId(Long requestId, Integer requestMemberStatus, Integer requestMemberOpeType);

    /**
     * 根据用章id和审批用户id获取
     * @param requestId
     * @param uid
     * @return
     */
    RequestMemberEntity getByRidUid(Long requestId, Long uid);

    /**
     * 获取数量
     * @param requestMemberQueryParam
     * @return
     */
    int getCount(RequestMemberQueryParam requestMemberQueryParam);
}

