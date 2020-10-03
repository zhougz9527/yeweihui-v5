package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.bfly.entity.BflySubVote;

import java.util.List;
import java.util.Map;


public interface BflySubVoteDao extends BaseMapper<BflySubVote> {

    List<Long> selectSubVoteId(Map<String, Long> params);

}
