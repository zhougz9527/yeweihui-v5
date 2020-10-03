package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.bfly.entity.BflyUserVote;
import org.apache.ibatis.annotations.Param;

import javax.crypto.MacSpi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyUserVoteService extends IService<BflyUserVote> {

    List<Map<String, Object>> selectUserVote(Map<String, Object> params);


    /**
     * 该投票的所有用户参会
     * @param bflyVoteId
     * @return
     */
    List<Map<String, Object>> meetingUserInfoByVoteId(Long bflyVoteId);

    /**
     * 该投票的所有用户投票
     * @param bflyVoteId
     * @return
     */
    List<Map<String, Object>> voteUserInfoByVoteId(Long bflyVoteId);

}
