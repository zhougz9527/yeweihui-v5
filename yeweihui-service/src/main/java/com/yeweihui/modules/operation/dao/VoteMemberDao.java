package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.operation.entity.VoteMemberEntity;
import com.yeweihui.modules.vo.query.VoteMemberQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表决成员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface VoteMemberDao extends BaseMapper<VoteMemberEntity> {

    List<VoteMemberEntity> simpleList(VoteMemberQueryParam voteMemberQueryParam);

    VoteMemberEntity getByVidUid(@Param("vid") Long vid, @Param("uid") Long uid, @Param("voteMemberType") int voteMemberType);

    int getCount(VoteMemberQueryParam voteMemberQueryParam);
}
