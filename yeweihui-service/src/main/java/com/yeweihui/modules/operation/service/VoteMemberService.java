package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.VoteMemberEntity;
import com.yeweihui.modules.vo.query.VoteMemberQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 表决成员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface VoteMemberService extends IService<VoteMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<VoteMemberEntity> simpleList(VoteMemberQueryParam voteMemberQueryParam);

    VoteMemberEntity getByVidUid(Long vid, Long uid, int voteMemberType);

    int getCount(VoteMemberQueryParam voteMemberQueryParam);
}

