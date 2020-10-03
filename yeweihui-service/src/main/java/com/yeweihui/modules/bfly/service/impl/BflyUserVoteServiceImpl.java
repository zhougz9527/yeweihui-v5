package com.yeweihui.modules.bfly.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.bfly.dao.BflyUserVoteDao;
import com.yeweihui.modules.bfly.entity.BflyUserVote;
import com.yeweihui.modules.bfly.service.BflyUserVoteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BflyUserVoteServiceImpl extends ServiceImpl<BflyUserVoteDao, BflyUserVote> implements BflyUserVoteService {


    @Override
    public List<Map<String, Object>> selectUserVote(Map<String, Object> params) {
        return this.baseMapper.selectUserVote(params);
    }

    @Override
    public List<Map<String, Object>> meetingUserInfoByVoteId(Long bflyVoteId) {
        return this.baseMapper.meetingUserInfoByVoteId(bflyVoteId);
    }

    @Override
    public List<Map<String, Object>> voteUserInfoByVoteId(Long bflyVoteId) {
        return this.baseMapper.voteUserInfoByVoteId(bflyVoteId);
    }


}
