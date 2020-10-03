package com.yeweihui.modules.bfly.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.bfly.dao.BflyUserSubVoteDao;
import com.yeweihui.modules.bfly.entity.BflyStatVote;
import com.yeweihui.modules.bfly.entity.BflyUserSubVote;
import com.yeweihui.modules.bfly.service.BflyUserSubVoteService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BflyUserSubVoteServiceImpl extends ServiceImpl<BflyUserSubVoteDao, BflyUserSubVote> implements BflyUserSubVoteService {


    @Override
    public List<Map<String, Object>> stateUserVote(HashMap<String, List> params) {
        return this.baseMapper.stateUserVote(params);
    }

    @Override
    public Map<String, Object> stateUserAreaVote(Map<String, Object> params) {
        return this.baseMapper.stateUserAreaVote(params);
    }

    @Override
    public Map<String, Object> selectUserSubVoteResultByTitle(Map<String, Object> params) {
        return this.baseMapper.selectUserSubVoteResultByTitle(params);
    }

}
