package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.bfly.entity.BflyStatVote;
import com.yeweihui.modules.bfly.entity.BflyUserSubVote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyUserSubVoteService extends IService<BflyUserSubVote> {

    List<Map<String, Object>> stateUserVote(HashMap<String, List> params);

    Map<String, Object> stateUserAreaVote(Map<String, Object> params);

    Map<String, Object> selectUserSubVoteResultByTitle(Map<String, Object> params);

}
