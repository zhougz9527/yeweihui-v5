package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.operation.entity.NoticeMemberEntity;
import com.yeweihui.modules.vo.query.NoticeMemberQueryParam;

import java.util.List;

public interface NoticeMemberDao extends BaseMapper<NoticeMemberEntity> {


    List<NoticeMemberEntity> simpleList(NoticeMemberQueryParam noticeMemberQueryParam);
}
