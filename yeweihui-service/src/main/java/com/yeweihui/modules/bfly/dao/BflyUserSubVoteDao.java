package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.bfly.entity.BflyUserSubVote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyUserSubVoteDao extends BaseMapper<BflyUserSubVote> {

    List<Map<String, Object>>stateUserVote(HashMap<String, List> params);

    Map<String, Object> stateUserAreaVote(Map<String, Object> params);

    Map<String, Object> selectUserSubVoteResultByTitle(Map<String, Object> params);

}
