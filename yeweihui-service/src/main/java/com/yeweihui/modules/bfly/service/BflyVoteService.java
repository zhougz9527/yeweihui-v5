package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.entity.BflyUserVoteInfo;
import com.yeweihui.modules.bfly.entity.BflyVote;
import com.yeweihui.modules.vo.bfly.form.vote.VoteForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyVoteService extends IService<BflyVote> {

    void saveVote(BflyVote bflyVote);

    BflyVote updateVote(BflyVote bflyVote);

    PageUtils<BflyUserVoteInfo> userVoteList(Long zoneId, String nickname, String title, String userName, Integer [] status, Integer current, Integer size);

    // 我的投票列表
    Map<String, List<Map<String,Object>>> myVotes(Long userId);


    /**
     * 我的已完成参会和投票
     * @param userId
     * @return
     */
    Map<String, List<Map<String,Object>>> myCompletedVotes(Long userId);

    // 历史投票
    List<BflyVote> historyVote(Long zoneId);

    // 提交参会
    void submitMeeting(VoteForm voteForm);

    BflyVote curInfo(Long id);

    // 表决详情
    BflyVote info(Long id);

    // 用户表决详情
    BflyVote info(Long id, Long userId) throws Exception;

    // 提交表决投票
    void submitVote(VoteForm voteForm) throws Exception;

    /**
     * 在投票已经开始的情况下，向某人发送某会议已经可以投票的消息
     * @param uid
     * @param voteId
     */
    void pushMessageToPerson(Long uid, Long voteId);

    // 定时任务更新表决的状态
    void crontabUpdateVote();

    // 批量更新表决状态
    void batchUpdateVoteStatus(List<BflyVote> voteList, Integer status);

    // 投票统计结果
    void voteResult(Long voteId);


    BflyVote selectStateByBflyVoteId(Long bflyVoteId);

    //查询投票列表
    HashMap<String, Object> selectVoteList(Long zoneId, String title, String startDate, String endDate, Integer page, Integer size);

    //查询实时投票列表
    HashMap<String, Object> selectCurVoteList(Long zoneId, String title, String startDate, String endDate, Integer page, Integer size);

    //查询用户表决详情
    BflyVote searchUserVoteDetail(Long userId, Long voteId) throws Exception;

    void updateBflyVoteOfflineVoteResultUrl(Long id, String offlineVoteResultUrl);


}
