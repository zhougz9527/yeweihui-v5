package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.bfly.entity.BflyUserVote;

import java.util.List;
import java.util.Map;

public interface BflyUserVoteDao extends BaseMapper<BflyUserVote> {

    List<Map<String, Object>> selectUserVote(Map<String, Object> params);

    Map<String,Object> searchAreaAndCount(Long zoneId,Long voteId);

    List<Map<String, Object>> meetingUserInfoByVoteId(Long voteId);

    List<Map<String, Object>> voteUserInfoByVoteId(Long voteId);
}
