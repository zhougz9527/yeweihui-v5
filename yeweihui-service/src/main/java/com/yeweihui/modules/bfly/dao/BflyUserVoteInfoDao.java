package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.bfly.entity.BflyUserVoteInfo;

import java.util.List;
import java.util.Map;

public interface BflyUserVoteInfoDao extends BaseMapper<BflyUserVoteInfo> {

//    List<BflyUserVoteInfo> selectPageVO(Page page, Map<String, Object> params);

    List<BflyUserVoteInfo> selectPageVOFromSnapshot(Page page, Map<String, Object> params);
}
