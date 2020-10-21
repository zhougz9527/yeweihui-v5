package com.yeweihui.modules.bfly.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.DateUtils;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.common.utils.RedisUtils;
import com.yeweihui.modules.CommonService;
import com.yeweihui.modules.bfly.dao.*;
import com.yeweihui.modules.bfly.entity.*;
import com.yeweihui.modules.bfly.service.*;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.bfly.form.vote.VoteForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.yeweihui.modules.enums.bfly.MagicEnum.*;
import static com.yeweihui.modules.enums.vote.VoteMemberTypeEnum.表决;

@Service("bflyVoteService")
public class BflyVoteServiceImpl extends ServiceImpl<BflyVoteDao, BflyVote> implements BflyVoteService {

    @Resource
    private BflySubVoteDao bflySubVoteDao;
    @Resource
    BflyUserSubVoteService bflyUserSubVoteService;
    @Resource
    private BflyVoteItemDao bflyVoteItemDao;
    @Resource
    private BflyUserVoteInfoDao bflyUserVoteInfoDao;
    @Resource
    private BflyUserVoteService bflyUserVoteService;
    @Resource
//    private BflyStatVoteDao bflyStatVoteDao;
    private BflyStateVoteService bflyStateVoteService;
    @Resource
    private BflyRoomService bflyRoomService;
    @Resource
    private BflyUserService bflyUserService;
    @Resource
    private BflyVoteDao bflyVoteDao;
    @Resource
    BflyRoomDao bflyRoomDao;
    @Resource
    BflyUserDao bflyUserDao;
    @Resource
    BflyUserVoteDao bflyUserVoteDao;
    @Resource
    ZonesService zonesService;
    @Resource
    WxMessageService wxMessageService;
    @Resource
    RedisUtils redisUtils;
    @Resource
    BflyRoomVoteSnapshotDao bflyRoomVoteSnapshotDao;
    @Resource
    CommonService commonService;

    @Value("${spring.profiles.active}")
    private String profiles;


    /**
     * 发起表决
     * @param bflyVote
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveVote(BflyVote bflyVote) {
        BflyVote beforeVote = this.selectOne(new EntityWrapper<BflyVote>().eq("title", bflyVote.getTitle()).eq("zone_id", bflyVote.getZoneId()));
        if (null != beforeVote) {
            throw new RRException("小区已存在相同的表决");
        }
        Date now = new Date();
        if (bflyVote.getMeetingStartTime().before(now)) {
            bflyVote.setStatus((Integer) 表决投票中.getCode());
        }

        //保存表决发起时小区总面积和总户数快照
        HashMap<String, Object> param = new HashMap<>();
        param.put("zoneId", bflyVote.getZoneId());
        List<Map<String, Object>> allAreaList = bflyRoomService.stateRoomAreaByZoneId(param);
        double allArea = 0.00;
        for (int i = 0; i < allAreaList.size(); i++) {
            allArea += Double.parseDouble(String.valueOf(allAreaList.get(i).get("total")));
        }
        BigDecimal d = new BigDecimal(new Double(allArea)).setScale(2,BigDecimal.ROUND_HALF_UP);
        bflyVote.setTotalAreaSnapshot(d);

        List<Map<String, Long>> totalRoomNumMap = bflyRoomService.stateRoomNumByZoneId(param);
        bflyVote.setRoomNumSnapshot(totalRoomNumMap.size());

        this.baseMapper.insert(bflyVote);
       bflyVote.getBflySubVotes().forEach(bflySubVote -> {
            bflySubVote.setBflyVoteId(bflyVote.getId());
            if (null == bflySubVote.getOptions() && null != bflySubVote.getBflyVoteItems()) {
                JSONArray options = new JSONArray();
                bflySubVote.getBflyVoteItems().forEach(bflyVoteItem -> {
                    options.add(bflyVoteItem.getMatter());
                });
                bflySubVote.setOptions(options.toJSONString());
            }

            this.bflySubVoteDao.insert(bflySubVote);
            if (null != bflySubVote.getBflyVoteItems()) {
                bflySubVote.getBflyVoteItems().forEach(bflyVoteItem -> {
                    bflyVoteItem.setBflySubVoteId(bflySubVote.getId());
                    this.bflyVoteItemDao.insert(bflyVoteItem);
                });
            }
        });

        List<BflyUser> bflyUserList = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("zone_id", bflyVote.getZoneId()).eq("status", 1).eq("is_valid", 1));
        ZonesEntity zonesEntity = zonesService.selectById(bflyVote.getZoneId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bflyUserList.forEach(bflyUser -> {
            BflyUserVote bflyUserVote = new BflyUserVote();
            bflyUserVote.setBflyUserId(bflyUser.getId());
            bflyUserVote.setBflyVoteId(bflyVote.getId());
            bflyUserVote.setStatus((Integer) 用户表决未参会.getCode());
            bflyUserVoteService.insert(bflyUserVote);

            //记录业主此时的投票和房屋信息对应的快照
            BflyRoom bflyRoom = bflyRoomDao.selectById(bflyUser.getBflyRoomId());
            BflyRoomVoteSnapshot bflyRoomVoteSnapshot = new BflyRoomVoteSnapshot(bflyRoom);
            bflyRoomVoteSnapshot.setVoteId(bflyVote.getId());
            bflyRoomVoteSnapshot.setUserVoteId(bflyUserVote.getId());
            bflyRoomVoteSnapshot.setZoneId(bflyUser.getZoneId());
            bflyRoomVoteSnapshotDao.insert(bflyRoomVoteSnapshot);

            // 推送小区业主公众号消息
            if (bflyVote.getStatus().equals(表决投票中.getCode())) {
                BflyVoteServiceImpl.this.pushMsgToUser(bflyUser, bflyVote, zonesEntity, simpleDateFormat);
            }
        });

        new Timer().schedule(new TimerTask() {

            int i=0;
            public void run() {

                i = i+1;
                if (i>=3)return;

                List<BflyUser> bflyUserList = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("zone_id", bflyVote.getZoneId()).eq("status", 1).eq("is_valid", 1));
                ZonesEntity zonesEntity = zonesService.selectById(bflyVote.getZoneId());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                bflyUserList.forEach(bflyUser -> {
                    BflyUserVote bflyUserVote = new BflyUserVote();
                    bflyUserVote.setBflyUserId(bflyUser.getId());
                    bflyUserVote.setBflyVoteId(bflyVote.getId());
                    bflyUserVote.setStatus((Integer) 用户表决未参会.getCode());
                    bflyUserVoteService.insert(bflyUserVote);

                    //记录业主此时的投票和房屋信息对应的快照
                    BflyRoom bflyRoom = bflyRoomDao.selectById(bflyUser.getBflyRoomId());
                    BflyRoomVoteSnapshot bflyRoomVoteSnapshot = new BflyRoomVoteSnapshot(bflyRoom);
                    bflyRoomVoteSnapshot.setVoteId(bflyVote.getId());
                    bflyRoomVoteSnapshot.setUserVoteId(bflyUserVote.getId());
                    bflyRoomVoteSnapshot.setZoneId(bflyUser.getZoneId());
                    bflyRoomVoteSnapshotDao.insert(bflyRoomVoteSnapshot);

                    // 推送小区业主公众号消息
                    if (bflyVote.getStatus().equals(表决投票中.getCode())) {
                        BflyVoteServiceImpl.this.pushMsgToUser(bflyUser, bflyVote, zonesEntity, simpleDateFormat);
                    }
                });

            }
        }, 1000*60 , 1000);


    }

    private void pushMsgToUser(BflyUser bflyUser, BflyVote bflyVote, ZonesEntity zonesEntity, SimpleDateFormat simpleDateFormat) {
        if (StringUtils.isNotEmpty(bflyUser.getPublicOpenId())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("touser", bflyUser.getPublicOpenId());
                jsonObject.put("template_id", "vQb6UpU6hz3H_4vSUSgHFB_uK8vEM2T0EplJuLZfXic");
                JSONObject accObj = new JSONObject();
                accObj.put("appid", "wxc4d2707a8de14c89");
                accObj.put("pagepath", "pages/home/home");
                jsonObject.put("miniprogram", accObj);
                JSONObject data = new JSONObject();
                JSONObject first = new JSONObject();
                first.put("value", "业主大会有新会议邀请您参加。");
                JSONObject keyword1 = new JSONObject();
                keyword1.put("value", bflyVote.getTitle());
                JSONObject keyword2 = new JSONObject();
                keyword2.put("value", zonesEntity.getName());
                JSONObject keyword3 = new JSONObject();
                keyword3.put("value", simpleDateFormat.format((bflyVote.getMeetingStartTime()).getTime()));
                JSONObject keyword4 = new JSONObject();
                keyword4.put("value", simpleDateFormat.format((bflyVote.getMeetingEndTime()).getTime()));
                JSONObject remark = new JSONObject();
                remark.put("value", "请您点击本通知进入小程序进行参会和投票");
                data.put("first", first);
                data.put("keyword1", keyword1);
                data.put("keyword2", keyword2);
                data.put("keyword3", keyword3);
                data.put("keyword4", keyword4);
                data.put("remark", remark);
                jsonObject.put("data", data);
                wxMessageService.pushMessage(jsonObject);
            }
    }

    @Override
    public BflyVote updateVote(BflyVote bflyVote) {
        this.baseMapper.updateById(bflyVote);
        return bflyVote;
    }

    @Override
    public PageUtils<BflyUserVoteInfo> userVoteList(Long zoneId,String nickname,String title,String userName,Integer [] status,Integer current,Integer size) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("zoneId",zoneId);
        params.put("nickname",nickname);
        params.put("title",title);
        params.put("userName", userName);
        params.put("statusList",status);
        Page page = new Query<BflyUserVoteInfo>(params).getPage();
        page.setCurrent(current);
        page.setSize(size);
        List<BflyUserVoteInfo> bflyUserVoteInfos = bflyUserVoteInfoDao.selectPageVOFromSnapshot(page, params);
        page.setRecords(bflyUserVoteInfos);
        return new PageUtils<BflyUserVoteInfo>(page);
    }



    /**
     * 我的投票列表
     * @param userId
     * @return
     */
    @Override
    public Map<String, List<Map<String,Object>>> myVotes(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("isShow", 表决显示.getCode());
        params.put("userId", userId);
        // 待参会
        List<Object> todoMeetCodeList = new ArrayList<>();
        todoMeetCodeList.add(表决参会中.getCode());
        todoMeetCodeList.add(表决投票中.getCode());
        params.put("userVoteStatus", 用户表决未参会.getCode());
        params.put("voteStatus", todoMeetCodeList);
        List<Map<String, Object>> todoMeetVotes = bflyUserVoteService.selectUserVote(params);

        // 待投票
        List<Object> votingCodeList = new ArrayList<>();
        votingCodeList.add(表决投票中.getCode());
        params.put("userVoteStatus", 用户表决已参会.getCode());
        params.put("voteStatus", votingCodeList);
        List<Map<String, Object>> voting = bflyUserVoteService.selectUserVote(params);

        // 已投票
        List<Object> votedCodeList = new ArrayList<>();
        votedCodeList.add(表决投票中.getCode());
        votedCodeList.add(表决投票结束.getCode());
        params.put("userVoteStatus", 用户表决已投票.getCode());
        params.put("voteStatus", votedCodeList);
        List<Map<String, Object>> votedList = bflyUserVoteService.selectUserVote(params);

        Map<String, List<Map<String,Object>>> resultMap = new HashMap<>();// 结果Map
        resultMap.put("todoMeetVotes", todoMeetVotes);
        resultMap.put("voting", voting);
        resultMap.put("voted", votedList);
        return resultMap;
    }

    @Override
    public Map<String, List<Map<String, Object>>> myCompletedVotes(Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("isShow", 表决显示.getCode());
        params.put("userId", userId);
        // 已参会
        List<Object> votingCodeList = new ArrayList<>();
        votingCodeList.add(表决投票中.getCode());
        votingCodeList.add(表决投票结束.getCode());
        params.put("userVoteStatus", 用户表决已参会.getCode());
        params.put("voteStatus", votingCodeList);
        List<Map<String, Object>> voting = bflyUserVoteService.selectUserVote(params);

        // 已投票
        List<Object> votedCodeList = new ArrayList<>();
        votedCodeList.add(表决投票中.getCode());
        votedCodeList.add(表决投票结束.getCode());
        params.put("userVoteStatus", 用户表决已投票.getCode());
        params.put("voteStatus", votedCodeList);
        List<Map<String, Object>> votedList = bflyUserVoteService.selectUserVote(params);

        // 结果Map
        Map<String, List<Map<String,Object>>> resultMap = new HashMap<>();
        resultMap.put("voting", voting);
        resultMap.put("voted", votedList);
        return resultMap;
    }

    /**
     * 历史投票
     *
     * @param zoneId
     * @return
     */
    @Override
    public List<BflyVote> historyVote(Long zoneId) {
        LinkedList<String> orderBy = new LinkedList<>();
        orderBy.add("updated_at");
        return this.baseMapper.selectList(
                new EntityWrapper<BflyVote>()
                        .setSqlSelect("id", "title")
                        .eq("zone_id", zoneId)
                        .eq("status", 表决投票结束.getCode())
                        .eq("is_show", 表决显示.getCode())
                        .orderDesc(orderBy)
        );
    }

    /**
     * 更新用户投票
     * @param voteForm
     */
    @Override
    public void submitMeeting(VoteForm voteForm) {
        Long userId = voteForm.getUserId();
        BflyUserVote bflyUserVote = bflyUserVoteService.selectOne(new EntityWrapper<BflyUserVote>().eq("bfly_user_id", userId).eq("bfly_vote_id", voteForm.getBflyVoteId()));
        if (null != bflyUserVote) {
            bflyUserVote.setMeetingSignatureUrl(voteForm.getMeetingSignatureUrl());
            bflyUserVote.setMeetingSignatureHeaderUrl(voteForm.getMeetingSignatureHeaderUrl());
            Date nowDate = new Date(System.currentTimeMillis());
            bflyUserVote.setMeetingSubmitTime(nowDate);
            bflyUserVote.setStatus((Integer) 用户表决已参会.getCode());
            bflyUserVoteService.updateById(bflyUserVote);
        }

    }

    /**
     * 表决信息
     * @param id
     * @return
     */
    @Override
    public BflyVote info(Long id) {
        BflyVote bflyVote = this.baseMapper.selectById(id);
        if (null == bflyVote) {
            throw new RRException("表决不存在");
        }
        List<BflySubVote> bflySubVotes = bflySubVoteDao.selectList(new EntityWrapper<BflySubVote>().eq("bfly_vote_id", bflyVote.getId()));
        bflySubVotes.forEach(bflySubVote -> {
            bflySubVote.setBflyVoteItems(bflyVoteItemDao.selectList(new EntityWrapper<BflyVoteItem>().eq("bfly_sub_vote_id", bflySubVote.getId())));
            bflySubVote.setBflyStatVote(bflyStateVoteService.selectOne(new EntityWrapper<BflyStatVote>().eq("bfly_vote_id", id).eq("bfly_sub_vote_id", bflySubVote.getId())));
        });
        bflyVote.setBflySubVotes(bflySubVotes);
        return bflyVote;
    }

    /**
     * 用户表决详情
     * @param id
     * @param userId
     * @return
     */
    @Override
    public BflyVote info(Long id, Long userId) throws Exception {
        BflyVote bflyVote = this.info(id);
        if (null == bflyVote) {
            throw new RRException("没有该表决的信息");
        }
        BflyUserVote bflyUserVote = bflyUserVoteService.selectOne(new EntityWrapper<BflyUserVote>().eq("bfly_user_id", userId).eq("bfly_vote_id", id));
        bflyVote.setBflyUserVote(bflyUserVote);
        bflyVote.getBflySubVotes().forEach(bflySubVote -> {
            BflyUserSubVote bflyUserSubVote = bflyUserSubVoteService.selectOne(new EntityWrapper<BflyUserSubVote>()
                    .eq("bfly_user_id", userId).eq("bfly_sub_vote_id", bflySubVote.getId()));
            bflySubVote.setBflyUserSubVote(bflyUserSubVote);
            //防止PDF模版加载参数时出现空指针异常
            if (null == bflySubVote.getDescription()) {
                bflySubVote.setDescription("");
            }

        });
        return bflyVote;
    }

    /**
     * 提交表决投票
     * @param voteForm
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitVote(VoteForm voteForm) throws Exception {
        Date now = new Date(System.currentTimeMillis());
        BflyVote bflyVote = this.info(voteForm.getBflyVoteId());
        if (bflyVote.getStatus() != 表决投票中.getCode()) {
            throw new RRException("表决目前不在投票阶段");
        }
        BflyUserVote bflyUserVote = bflyUserVoteService.selectOne(new EntityWrapper<BflyUserVote>()
                .eq("bfly_user_id", voteForm.getUserId()).eq("bfly_vote_id", voteForm.getBflyVoteId()));
        if (null == bflyUserVote){
            throw new RRException("用户表决数据为空");
        }
        bflyUserVote.setVoteSignatureHeaderUrl(voteForm.getVoteSignatureHeaderUrl());
        bflyUserVote.setVoteSignatureUrl(voteForm.getVoteSignatureUrl());
        bflyUserVote.setVoteSubmitTime(now);
        bflyUserVote.setStatus((Integer) 用户表决已投票.getCode());
        bflyUserVoteService.updateById(bflyUserVote);
        voteForm.getUserSubVotes().forEach(userSubVoteForm -> bflyUserSubVoteService.insertOrUpdate(userSubVoteForm));
    }

    @Override
    public void pushMessageToPerson(Long uid, Long voteId) {
        BflyUser bflyUser = bflyUserService.selectById(uid);
        BflyVote bflyVote = bflyVoteDao.selectById(voteId);
        if (!bflyVote.getStatus().equals(表决投票中.getCode()) || bflyVote.getVoteStartTime().after(new Date())) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotEmpty(bflyUser.getPublicOpenId())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("touser", bflyUser.getPublicOpenId());
                jsonObject.put("template_id", "IVbRiTdn7YqjGtzMiijEaVTfOdKMAAtDpdrLNaY7TyU");
                JSONObject accObj = new JSONObject();
                accObj.put("appid", "wxc4d2707a8de14c89");
                accObj.put("pagepath", "pages/home/home");
                jsonObject.put("miniprogram", accObj);
                JSONObject data = new JSONObject();
                JSONObject first = new JSONObject();
                first.put("value", "业主大会有新的投票请您参加。");
                JSONObject keyword1 = new JSONObject();
                keyword1.put("value", bflyVote.getTitle());
                JSONObject keyword2 = new JSONObject();
                keyword2.put("value", simpleDateFormat.format((bflyVote.getVoteStartTime()).getTime()));
                JSONObject remark = new JSONObject();
                remark.put("value", "请您点击本通知进入小程序进行表决或投票。");
                data.put("first", first);
                data.put("keyword1", keyword1);
                data.put("keyword2", keyword2);
                data.put("remark", remark);
                jsonObject.put("data", data);
                wxMessageService.pushMessage(jsonObject);
            }
    }

    /**
     * 定时任务更新表决的状态
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void crontabUpdateVote() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp tomorrow = new Timestamp(now.getTime() + 1000 * 60 * 60 * 24);
        // 未开始参会的表决
        List<BflyVote> unStartMeetingVoteList = this.selectList(new EntityWrapper<BflyVote>().eq("status", (Integer)表决未开始.getCode()).le("meeting_start_time", now));
        this.batchUpdateVoteStatus(unStartMeetingVoteList, (Integer)表决投票中.getCode());
        // 参会中的表决
//        List<BflyVote> meetingVoteList = this.selectList(new EntityWrapper<BflyVote>().eq("status", (Integer)表决参会中.getCode()).le("meeting_end_time", now));
//        this.batchUpdateVoteStatus(meetingVoteList, (Integer)表决参会结束.getCode());
        // 要开始投票的表决
        List<BflyVote> unStartVoteList = this.selectList(new EntityWrapper<BflyVote>().eq("status", (Integer)表决投票中.getCode()).le("vote_start_time", now));
//        this.batchUpdateVoteStatus(unStartVoteList, (Integer) 表决投票中.getCode());
        //离结束还有24小时的投票
        List<BflyVote> voteToEndTomorrow = this.selectList(
                new EntityWrapper<BflyVote>()
                        .eq("status", (Integer) 表决投票中.getCode())
                        .le("vote_end_time", tomorrow)
                        .le("vote_start_time", now));

        // 要结束投票的表决
        List<BflyVote> votingList = this.selectList(new EntityWrapper<BflyVote>().eq("status", (Integer)表决投票中.getCode()).le("vote_end_time", now));
        this.batchUpdateVoteStatus(votingList, (Integer) 表决投票结束.getCode());
        votingList.forEach(bflyVote -> this.voteResult(bflyVote.getId()));
//        wxMessageService.getPublicOpenIds();

        unStartMeetingVoteList.forEach(bflyVote -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<BflyUser> bflyUserList = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("zone_id", bflyVote.getZoneId()).eq("status", 1).eq("is_valid", 1));
            ZonesEntity zonesEntity = zonesService.selectById(bflyVote.getZoneId());
            bflyUserList.forEach(bflyUser -> {
                if (StringUtils.isNotEmpty(bflyUser.getPublicOpenId())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("touser", bflyUser.getPublicOpenId());
                    jsonObject.put("template_id", "vQb6UpU6hz3H_4vSUSgHFB_uK8vEM2T0EplJuLZfXic");
                    JSONObject accObj = new JSONObject();
                    accObj.put("appid", "wxc4d2707a8de14c89");
                    accObj.put("pagepath", "pages/home/home");
                    jsonObject.put("miniprogram", accObj);
                    JSONObject data = new JSONObject();
                    JSONObject first = new JSONObject();
                    first.put("value", "业主大会有新会议邀请您参加。");
                    JSONObject keyword1 = new JSONObject();
                    keyword1.put("value", bflyVote.getTitle());
                    JSONObject keyword2 = new JSONObject();
                    keyword2.put("value", zonesEntity.getName());
                    JSONObject keyword3 = new JSONObject();
                    keyword3.put("value", simpleDateFormat.format((bflyVote.getMeetingStartTime()).getTime()));
                    JSONObject keyword4 = new JSONObject();
                    keyword4.put("value", simpleDateFormat.format((bflyVote.getMeetingEndTime()).getTime()));
                    JSONObject remark = new JSONObject();
                    remark.put("value", "请您点击本通知进入小程序进行参会和投票");
                    data.put("first", first);
                    data.put("keyword1", keyword1);
                    data.put("keyword2", keyword2);
                    data.put("keyword3", keyword3);
                    data.put("keyword4", keyword4);
                    data.put("remark", remark);
                    jsonObject.put("data", data);
                    wxMessageService.pushMessage(jsonObject);
                }
            });
        });

        unStartVoteList.forEach(bflyVote -> {
            String key = "startVoteAlreadyPush-" + profiles;
            String ids = redisUtils.get(key);
            boolean isPush = true;
            if (null != ids && StringUtils.isNotEmpty(ids)) {
                Set<String> idSet = new HashSet<>(Arrays.asList(StringUtils.split(ids, ",")));
                if (idSet.contains(String.valueOf(bflyVote.getId()))) {
                    isPush = false;
                } else {
                    idSet.add(String.valueOf(bflyVote.getId()));
                    String newIds = StringUtils.join(idSet, ",");
                    redisUtils.set(key, newIds, -1);
                }
            } else {
                redisUtils.set(key, String.valueOf(bflyVote.getId()), -1);
            }
            if (isPush) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                List<BflyUser> bflyUserList = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("zone_id", bflyVote.getZoneId()).eq("status", 1).eq("is_valid", 1));
                List<BflyUser> bflyUserList = bflyUserDao.findBflyUserByVoteIdAndUserVoteStatus(bflyVote.getId(), (Integer) 用户表决已参会.getCode());
                bflyUserList.forEach(bflyUser -> {
                    if (StringUtils.isNotEmpty(bflyUser.getPublicOpenId())) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("touser", bflyUser.getPublicOpenId());
                        jsonObject.put("template_id", "IVbRiTdn7YqjGtzMiijEaVTfOdKMAAtDpdrLNaY7TyU");
                        JSONObject accObj = new JSONObject();
                        accObj.put("appid", "wxc4d2707a8de14c89");
                        accObj.put("pagepath", "pages/home/home");
                        jsonObject.put("miniprogram", accObj);
                        JSONObject data = new JSONObject();
                        JSONObject first = new JSONObject();
                        first.put("value", "业主大会有新的投票请您参加。");
                        JSONObject keyword1 = new JSONObject();
                        keyword1.put("value", bflyVote.getTitle());
                        JSONObject keyword2 = new JSONObject();
                        keyword2.put("value", simpleDateFormat.format((bflyVote.getVoteStartTime()).getTime()));
                        JSONObject remark = new JSONObject();
                        remark.put("value", "请您点击本通知进入小程序进行表决或投票。");
                        data.put("first", first);
                        data.put("keyword1", keyword1);
                        data.put("keyword2", keyword2);
                        data.put("remark", remark);
                        jsonObject.put("data", data);
                        wxMessageService.pushMessage(jsonObject);
                    }
                });
            }
        });

        voteToEndTomorrow.forEach(bflyVote ->{
            String key = "startVoteEndPromptPush-" + profiles;
            String ids = redisUtils.get(key);
            boolean push = true;
            if (null != ids && StringUtils.isNotEmpty(ids)) {
                Set<String> idSet = new HashSet<>(Arrays.asList(StringUtils.split(ids, ",")));
                if (idSet.contains(String.valueOf(bflyVote.getId()))) {
                    push = false;
                } else {
                    idSet.add(String.valueOf(bflyVote.getId()));
                    String newIds = StringUtils.join(idSet, ",");
                    redisUtils.set(key, newIds, -1);
                }
            } else {
                redisUtils.set(key, String.valueOf(bflyVote.getId()), -1);
            }
            if (push) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                List<BflyUser> bflyUserList = bflyUserDao.findBflyUserByVoteIdAndUserVoteStatus(bflyVote.getId(), (Integer) 用户表决已参会.getCode());
                bflyUserList.forEach(bflyUser -> {
                    if (StringUtils.isNotEmpty(bflyUser.getPublicOpenId())) {
                        commonService.sendMsg(bflyUser.getPhoneNum(), "SMS_194051130");
                    }
                });
            }
        });


        votingList.forEach(bflyVote -> {
            List<BflyUser> bflyUserList = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("zone_id", bflyVote.getZoneId()).eq("status", 1).eq("is_valid", 1));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bflyUserList.forEach(bflyUser -> {
                if (StringUtils.isNotEmpty(bflyUser.getPublicOpenId())) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("touser", bflyUser.getPublicOpenId());
                    jsonObject.put("template_id", "IVbRiTdn7YqjGtzMiijEaVTfOdKMAAtDpdrLNaY7TyU");
                    JSONObject accObj = new JSONObject();
                    accObj.put("appid", "wxc4d2707a8de14c89");
                    accObj.put("pagepath", "pages/voteHistoryDetail/index?voteId=" + bflyVote.getId());
                    jsonObject.put("miniprogram", accObj);
                    JSONObject data = new JSONObject();
                    JSONObject first = new JSONObject();
                    first.put("value", "本次会议已结束。");
                    JSONObject keyword1 = new JSONObject();
                    keyword1.put("value", bflyVote.getTitle());
                    JSONObject keyword2 = new JSONObject();
                    keyword2.put("value", simpleDateFormat.format((bflyVote.getVoteStartTime()).getTime()) + " 至 " + simpleDateFormat.format((bflyVote.getVoteEndTime()).getTime()));
                    JSONObject remark = new JSONObject();
                    remark.put("value", "请点击本通知查看会议结果");
                    data.put("first", first);
                    data.put("keyword1", keyword1);
                    data.put("keyword2", keyword2);
                    data.put("remark", remark);
                    jsonObject.put("data", data);
                    wxMessageService.pushMessage(jsonObject);
                }
            });
        });
    }



    /**
     * 批量更新表决的状态
     * @param voteList
     * @param status
     */
    @Override
    public void batchUpdateVoteStatus(List<BflyVote> voteList, Integer status) {
        if (null != voteList && voteList.size() > 0) {
            voteList.forEach(bflyVote -> bflyVote.setStatus(status));
            this.updateBatchById(voteList);
        }
    }

    /**
     * 展示当前投票结果
     * @param voteId
     * @return
     */
    @Override
    public BflyVote curInfo(Long voteId) {
        BflyVote bflyVote = this.info(voteId);
        if (bflyVote.getBflyStatVoteList() != null && bflyVote.getBflyStatVoteList().size() > 0) {
            return bflyVote;
        }
//        List<BflySubVote> bflySubVotes = bflySubVoteDao.selectList(new EntityWrapper<BflySubVote>().eq("bfly_vote_id", voteId));




//        List<BflyUser> zoneUserList = bflyUserDao.selectList(new EntityWrapper<BflyUser>().eq("zoneId", bflyVote.getZoneId()));
        //
//        List<Map<String, Integer>> zoneUserList = bflyRoomService.snapshotRoomNumByVoteId(voteId);// 小区业主
//        List<Map<String, Object>> allAreaList = bflyRoomService.snapshotRoomAreaByVoteId(voteId);// 小区业主总面积
//        Map<String, Object> zoneParams = new HashMap<>();
//        zoneParams.put("zoneId", bflyVote.getZoneId());
//        List<Map<String, Integer>> zoneUserList = bflyRoomService.snapshotRoomNumByVoteId(bflyVote.getZoneId());// 小区业主
//        List<Map<String, Object>> allAreaList = bflyRoomService.stateRoomAreaByZoneId(zoneParams);// 小区业主总面积
        //

//        double allArea = 0.00;
//        for (int i = 0; i < allAreaList.size(); i++) {
//            allArea += Double.parseDouble(String.valueOf(allAreaList.get(i).get("total")));
//        }
//        BigDecimal d = new BigDecimal(new Double(allArea)).setScale(2,BigDecimal.ROUND_HALF_UP);


        int totalVoteQuantity = bflyVote.getRoomNumSnapshot();// 发起投票时小区业主人数
        BigDecimal totalAreaSnapshot = bflyVote.getTotalAreaSnapshot().setScale(2, BigDecimal.ROUND_HALF_UP); //发起投票时小区总面积

        List<BflySubVote> bflySubVotes = bflyVote.getBflySubVotes();
        bflySubVotes.forEach(bflySubVote -> {
                    String options = bflySubVote.getOptions();
                    JSONArray optionArray = JSONArray.parseArray(options);
                    List<BflyUserSubVote> bflyUserSubVotes = bflyUserSubVoteService.selectList(new EntityWrapper<BflyUserSubVote>().eq("bfly_sub_vote_id", bflySubVote.getId()));
                    List<String> resultOptions = new ArrayList<>();// 所有用户的投票结果
                    // 记录所有的投票结果
                    bflyUserSubVotes.forEach(bflyUserSubVote -> resultOptions.add(bflyUserSubVote.getResultOption()));

//            int totalVoteQuantity = resultOptions.size();// 投票总户数
                    JSONArray jsonArray = new JSONArray();// 投票户数统计
                    JSONArray areaArray = new JSONArray();// 投票面积统计
//            JSONArray meetingArray = new JSONArray();// 已参会统计
                    JSONArray uncertArray = new JSONArray();// 未认证用户统计

                    // 已参会Object
                    JSONObject meetingObject = new JSONObject();
                    Integer[] status = {(Integer) 用户表决已参会.getCode(), (Integer) 用户表决已投票.getCode()};
                    List<BflyUserVote> userVotes = bflyUserVoteService.selectList(new EntityWrapper<BflyUserVote>().eq("bfly_vote_id", voteId).in("status", status));
//            meetingObject.put("小区总户数", totalVoteQuantity);
                    meetingObject.put("已经参会户数", null == userVotes ? 0 : userVotes.size());
                    //已参会面积
                    Map<String, Object> meetingAreaMap = bflyUserDao.sumMeetingArea(bflyVote.getId());
                    if (null == meetingAreaMap || meetingAreaMap.size() == 0) {
                        meetingObject.put("已经参会的面积", new BigDecimal(0));
                    }else{
                        meetingObject.put("已经参会的面积", meetingAreaMap.get("total"));
                    }
                    // 未认证
                    JSONObject unsertObject = new JSONObject();
                    List<BflyRoomVoteSnapshot> snapshots = bflyRoomVoteSnapshotDao.selectList(
                            new EntityWrapper<BflyRoomVoteSnapshot>()
                                    .eq("vote_id", voteId)
                    );
                    //未认证面积
                    BigDecimal unCertArea = bflyVote.getTotalAreaSnapshot();
                    for (BflyRoomVoteSnapshot snapshot : snapshots) {
                        unCertArea = unCertArea.subtract(snapshot.getHousingArea());
                    }

                    unsertObject.put("未认证面积", unCertArea);
                    // 未认证用户
                    int uncertNum = totalVoteQuantity - snapshots.size();
                    unsertObject.put("未认证户数", uncertNum);
                    uncertArray.add(unsertObject);


                    Integer noVotingNum = (Integer) meetingObject.get("已经参会户数");
                    BigDecimal noVotingArea = (BigDecimal) meetingObject.get("已经参会的面积");
                    for (int i = 0; i < optionArray.size(); i++) {
                        String option = (String) optionArray.get(i);
                        // 户数
                        int count = Collections.frequency(resultOptions, option);
                        JSONObject jsonObject = new JSONObject();
        //                jsonObject.put("小区总户数", totalVoteQuantity);
                        jsonObject.put("选项", option);
                        jsonObject.put("投票户数", count);
                        noVotingNum = noVotingNum - count;
                        jsonArray.add(jsonObject);
                        // 面积
                        JSONObject areaObject = new JSONObject();
                        Map<String, Object> params = new HashMap<>();
                        params.put("bflySubVoteId", bflySubVote.getId());
                        params.put("resultOption", option);
                        Map<String, Object> area = bflyUserSubVoteService.stateUserAreaVote(params);
        //                areaObject.put("小区总面积", d);
                        areaObject.put("选项", option);
                        areaObject.put("投票面积", null == area ? new BigDecimal(0) : area.get("allArea"));
                        noVotingArea = noVotingArea.subtract((BigDecimal) areaObject.get("投票面积"));
                        areaArray.add(areaObject);
                    }
                    //设置已参会未投票的户数和面积
                    //户数
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("选项", "已参会未投票");
                    jsonObject.put("投票户数", noVotingNum);
                    jsonArray.add(jsonObject);
                    // 面积
                    JSONObject areaObject = new JSONObject();
                    areaObject.put("选项", "已参会未投票");
                    areaObject.put("投票面积", noVotingArea);
                    areaArray.add(areaObject);

                    BflyStatVote bflyStatVote = new BflyStatVote();
                    bflyStatVote.setBflyVoteId(voteId);
                    bflyStatVote.setBflySubVoteId(bflySubVote.getId());
                    bflyStatVote.setBflySubVoteTitle(bflySubVote.getTitle());
                    bflyStatVote.setTotalVoteQuantity(totalVoteQuantity);
                    bflyStatVote.setOptions(options);
                    bflyStatVote.setTotalVoteArea(totalAreaSnapshot);
                    bflyStatVote.setQuantityResultJson(jsonArray.toJSONString());
                    bflyStatVote.setAreaResultJson(areaArray.toJSONString());
                    bflyStatVote.setMeetingResultJson(meetingObject.toJSONString());
                    bflyStatVote.setUncertResultJson(uncertArray.toJSONString());

                    bflySubVote.setBflyStatVote(bflyStatVote);
                });
        return bflyVote;
    }

    /**
     * 统计表决结果
     * @param voteId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void voteResult(Long voteId) {
        BflyVote bflyVote = this.info(voteId);
        if (!bflyVote.getStatus().equals((Integer) 表决投票结束.getCode())) {
            throw new RRException("表决未结束");
        }
        //发起投票时小区信息
        int totalVoteQuantity = bflyVote.getRoomNumSnapshot();// 发起投票时小区业主人数
        BigDecimal totalAreaSnapshot = bflyVote.getTotalAreaSnapshot().setScale(2, BigDecimal.ROUND_HALF_UP); //发起投票时小区总面积

        // 子表决列表
        List<BflySubVote> bflySubVotes = bflyVote.getBflySubVotes();
        bflySubVotes.forEach(bflySubVote -> {
            String options = bflySubVote.getOptions();
            JSONArray optionArray = JSONArray.parseArray(options);
            List<BflyUserSubVote> bflyUserSubVotes = bflyUserSubVoteService.selectList(new EntityWrapper<BflyUserSubVote>().eq("bfly_sub_vote_id", bflySubVote.getId()));
            List<String> resultOptions = new ArrayList<>();// 所有用户的投票结果
            // 记录所有的投票结果
            bflyUserSubVotes.forEach(bflyUserSubVote -> resultOptions.add(bflyUserSubVote.getResultOption()));

//            int totalVoteQuantity = resultOptions.size();// 投票总户数
            JSONArray jsonArray = new JSONArray();// 投票户数统计
            JSONArray areaArray = new JSONArray();// 投票面积统计
//            JSONArray meetingArray = new JSONArray();// 已参会统计
            JSONArray uncertArray = new JSONArray();// 未认证用户统计

            // 已参会Object
            JSONObject meetingObject = new JSONObject();
            Integer[] status = {(Integer) 用户表决已参会.getCode(), (Integer) 用户表决已投票.getCode()};
            List<BflyUserVote> userVotes = bflyUserVoteService.selectList(new EntityWrapper<BflyUserVote>().eq("bfly_vote_id", voteId).in("status", status));
//            meetingObject.put("小区总户数", totalVoteQuantity);
            meetingObject.put("已经参会户数", null == userVotes ? 0 : userVotes.size());
            //已参会面积
            Map<String, Object> meetingAreaMap = bflyUserDao.sumMeetingArea(bflyVote.getId());
            if (null == meetingAreaMap || meetingAreaMap.size() == 0) {
                meetingObject.put("已经参会的面积", new BigDecimal(0));
            }else{
                meetingObject.put("已经参会的面积", meetingAreaMap.get("total"));
            }
            // 未认证
            JSONObject unsertObject = new JSONObject();
            List<BflyRoomVoteSnapshot> snapshots = bflyRoomVoteSnapshotDao.selectList(
                    new EntityWrapper<BflyRoomVoteSnapshot>()
                            .eq("vote_id", voteId)
            );
            //未认证面积
            BigDecimal unCertArea = bflyVote.getTotalAreaSnapshot();
            for (BflyRoomVoteSnapshot snapshot : snapshots) {
                unCertArea = unCertArea.subtract(snapshot.getHousingArea());
            }
            unsertObject.put("未认证面积", unCertArea);
            // 未认证用户
            int uncertNum = totalVoteQuantity - snapshots.size();
            unsertObject.put("未认证户数", uncertNum);
            uncertArray.add(unsertObject);

            Integer noVotingNum = (Integer) meetingObject.get("已经参会户数");
            BigDecimal noVotingArea = (BigDecimal) meetingObject.get("已经参会的面积");
            for (int i = 0; i < optionArray.size(); i++) {
                String option = (String) optionArray.get(i);
                // 户数
                int count = Collections.frequency(resultOptions, option);
                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("小区总户数", totalVoteQuantity);
                jsonObject.put("选项", option);
                jsonObject.put("投票户数", count);
                noVotingNum = noVotingNum - count;
                jsonArray.add(jsonObject);
                // 面积
                JSONObject areaObject = new JSONObject();
                Map<String, Object> params = new HashMap<>();
                params.put("bflySubVoteId", bflySubVote.getId());
                params.put("resultOption", option);
                Map<String, Object> area = bflyUserSubVoteService.stateUserAreaVote(params);
//                areaObject.put("小区总面积", d);
                areaObject.put("选项", option);
                areaObject.put("投票面积", null == area ? new BigDecimal(0) : area.get("allArea"));
                noVotingArea = noVotingArea.subtract((BigDecimal) areaObject.get("投票面积"));
                areaArray.add(areaObject);
            }
            //设置已参会未投票的户数和面积
            //户数
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("选项", "已参会未投票");
            jsonObject.put("投票户数", noVotingNum);
            jsonArray.add(jsonObject);
            // 面积
            JSONObject areaObject = new JSONObject();
            areaObject.put("选项", "已参会未投票");
            areaObject.put("投票面积", noVotingArea);
            areaArray.add(areaObject);

            BflyStatVote bflyStatVote = new BflyStatVote();
            bflyStatVote.setBflyVoteId(voteId);
            bflyStatVote.setBflySubVoteId(bflySubVote.getId());
            bflyStatVote.setBflySubVoteTitle(bflySubVote.getTitle());
            bflyStatVote.setTotalVoteQuantity(totalVoteQuantity);
            bflyStatVote.setOptions(options);
            bflyStatVote.setTotalVoteArea(totalAreaSnapshot);
            bflyStatVote.setQuantityResultJson(jsonArray.toJSONString());
            bflyStatVote.setAreaResultJson(areaArray.toJSONString());
            bflyStatVote.setMeetingResultJson(meetingObject.toJSONString());
            bflyStatVote.setUncertResultJson(uncertArray.toJSONString());
            bflyStateVoteService.insert(bflyStatVote);
        });

    }

    @Override
    public BflyVote selectStateByBflyVoteId(Long bflyVoteId) {
        BflyVote bflyVote = this.info(bflyVoteId);
        List<Long> subVoteIds = new ArrayList<>();
        bflyVote.getBflySubVotes().forEach(bflySubVote -> subVoteIds.add(bflySubVote.getId()));
        bflyVote.setBflyStatVoteList(bflyStateVoteService.selectList(new EntityWrapper<BflyStatVote>().in("bfly_sub_vote_id", subVoteIds)));
        return bflyVote;
    }


    /**
     * @Description: 查询投票列表
     * @Param:
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/16
     */
    @Override
    public HashMap<String, Object> selectVoteList(Long zoneId, String title, String startDate, String endDate, Integer page, Integer size) {
        Integer current = size * (page - 1);
        //转化日期格式
        String beginDate = DateUtils.format(DateUtils.getDateByUnknown(startDate), DateUtils.DATE_TIME_PATTERN);
        String overDate = DateUtils.format(DateUtils.getDateByUnknown(endDate), DateUtils.DATE_TIME_PATTERN);
        //查询结果
        List<BflyVote> bflyVotes = bflyVoteDao.queryBflyVoteList(zoneId, title, beginDate, overDate, current, size);
        Long totalCount = bflyVoteDao.queryBflyVoteListCount(zoneId, title, beginDate, overDate);
        for (BflyVote bflyVote : bflyVotes) {
            bflyVote.setBflyStatVoteList(bflyStateVoteService.selectList(new EntityWrapper<BflyStatVote>().eq("bfly_vote_id", bflyVote.getId())));
        }
        //封装结果集
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", bflyVotes);
        resultMap.put("totalCount", totalCount);
        resultMap.put("totalPage", (totalCount + size - 1)/size);
        resultMap.put("pageSize", size);
        resultMap.put("currPage", page);
        return resultMap;
    }

    /**
     * @Description: 查询实时投票列表
     * @Param:
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/16
     */
    @Override
    public HashMap<String, Object> selectCurVoteList(Long zoneId, String title, String startDate, String endDate, Integer page, Integer size) {
        Integer current = size * (page - 1);
        //转化日期格式
        String beginDate = DateUtils.format(DateUtils.getDateByUnknown(startDate), DateUtils.DATE_TIME_PATTERN);
        String overDate = DateUtils.format(DateUtils.getDateByUnknown(endDate), DateUtils.DATE_TIME_PATTERN);
        //查询结果
        List<BflyVote> bflyVotes = bflyVoteDao.queryCurBflyVoteList(zoneId, title, beginDate, overDate, current, size, 4);

        for (BflyVote bflyVote : bflyVotes) {
            bflyVote.setBflyStatVoteList(bflyStateVoteService.selectList(new EntityWrapper<BflyStatVote>().eq("bfly_vote_id", bflyVote.getId())));
        }
        Long totalCount = bflyVoteDao.queryCurBflyVoteListCount(zoneId, title, beginDate, overDate,(Integer)表决投票中.getCode());
        //封装结果集
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", bflyVotes);
        resultMap.put("totalCount", totalCount);
        resultMap.put("totalPage", (totalCount + size - 1)/size);
        resultMap.put("pageSize", size);
        resultMap.put("currPage", page);
        return resultMap;
    }
    
    /** 
    * @Description: 查询用户表决详情
    * @Param:
    * @return:  
    * @Author: tenaciousVine
    * @Date: 2020/3/18 
    */
    @Override
    public BflyVote searchUserVoteDetail(Long userId, Long voteId) throws Exception {
        BflyVote info = this.info(voteId, userId);
        //从快照中获取投票时用户的房屋信息，
        BflyUser bflyUser = new BflyUser();
        BflyRoomVoteSnapshot one = new BflyRoomVoteSnapshot();
        one.setUserVoteId(info.getBflyUserVote().getId());
        BflyRoomVoteSnapshot bflyRoomVoteSnapshot = bflyRoomVoteSnapshotDao.selectOne(one);
        if (null == bflyRoomVoteSnapshot) {
            throw new RRException("不存在该用户在该镖局创建时的房屋数据");
        }
        ZonesEntity zonesEntity = zonesService.selectById(bflyRoomVoteSnapshot.getZoneId());
        bflyUser.setZoneName(zonesEntity.getName());

        String address = zonesEntity.getName();
        if (StringUtils.isNotEmpty(bflyRoomVoteSnapshot.getCourt())) {
            address += bflyRoomVoteSnapshot.getCourt();
        }
        if (StringUtils.isNotEmpty(bflyRoomVoteSnapshot.getBuilding())) {
            if (bflyRoomVoteSnapshot.getBuilding().equals(SHOP.getName()) || bflyRoomVoteSnapshot.getBuilding().equals(HOUSE.getName())) {
                address += bflyRoomVoteSnapshot.getBuilding();
            } else {
                address += bflyRoomVoteSnapshot.getBuilding() + "幢";
            }
        }
        if (StringUtils.isNotEmpty(bflyRoomVoteSnapshot.getUnitName())) {
            address += bflyRoomVoteSnapshot.getUnitName() + "单元";
        }
        if (StringUtils.isNotEmpty(bflyRoomVoteSnapshot.getFloorName())) {
            address += bflyRoomVoteSnapshot.getFloorName();
        }
        bflyUser.setAddress(address);
        bflyUser.setHousingArea(bflyRoomVoteSnapshot.getHousingArea());
        bflyUser.setUsername(bflyRoomVoteSnapshot.getUserName());
        bflyUser.setCourt(bflyRoomVoteSnapshot.getCourt());
        bflyUser.setBuilding(bflyRoomVoteSnapshot.getBuilding());
        bflyUser.setFloorName(bflyRoomVoteSnapshot.getFloorName());
        bflyUser.setUnitName(bflyRoomVoteSnapshot.getUnitName());
        bflyUser.setUsername(bflyRoomVoteSnapshot.getUserName());

        info.getBflyUserVote().setBflyUser(bflyUser);
        return info;
    }

    @Override
    public void updateBflyVoteOfflineVoteResultUrl(Long id, String offlineVoteResultUrl) {
        bflyVoteDao.updateBflyVoteOfflineVoteResultUrl(id, offlineVoteResultUrl);
    }

}
