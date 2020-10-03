package com.yeweihui.modules.common.service.impl;

import com.yeweihui.modules.common.service.MainPageService;
import com.yeweihui.modules.enums.announce.paper.AnnounceStatusEnum;
import com.yeweihui.modules.enums.bill.BillMemberStatusEnum;
import com.yeweihui.modules.enums.bill.BillStatusEnum;
import com.yeweihui.modules.enums.meeting.MeetingMemberStatusEnum;
import com.yeweihui.modules.enums.meeting.MeetingStatusEnum;
import com.yeweihui.modules.enums.paper.PaperStatusEnum;
import com.yeweihui.modules.enums.request.RequestMemberVerifyStatusEnum;
import com.yeweihui.modules.enums.request.RequestOpeTypeEnum;
import com.yeweihui.modules.enums.request.RequestStatusEnum;
import com.yeweihui.modules.enums.request.RequestTypeEnum;
import com.yeweihui.modules.enums.vote.VoteMemberStatusEnum;
import com.yeweihui.modules.enums.vote.VoteMemberTypeEnum;
import com.yeweihui.modules.enums.vote.VoteStatusEnum;
import com.yeweihui.modules.operation.service.*;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.api.vo.MainPage1VO;
import com.yeweihui.modules.vo.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MainPageServiceImpl implements MainPageService {


    @Autowired
    ZonesService zonesService;

    @Autowired
    UserService userService;

    @Autowired
    RequestService requestService;

    @Autowired
    VoteService voteService;

    @Autowired
    PaperService paperService;

    @Autowired
    BillService billService;

    @Autowired
    MeetingService meetingService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    AnnounceService announceService;

    /**
     * 首页头部信息1（小区信息，待我用章审批数量，待我事项表决数量，网上发函数量，费用报销数量）
     * @param userIdQueryParam
     * @return
     */
    @Override
    public MainPage1VO mainPage1(UserIdQueryParam userIdQueryParam) {
        MainPage1VO mainPage1VO = new MainPage1VO();
        Long uid = userIdQueryParam.getUid();
        UserEntity userEntity = userService.selectById(uid);
        ZonesEntity zonesEntity = zonesService.selectById(userEntity.getZoneId());
        //小区信息
        mainPage1VO.setZonesEntity(zonesEntity);
        //待我用章审批数量
        RequestQueryParam requestQueryParam = new RequestQueryParam();
        requestQueryParam.setVerifyUid(uid);
        requestQueryParam.setZoneId(userEntity.getZoneId());
//        requestQueryParam.setRequestStatus(RequestStatusEnum.审核中.getCode());
        requestQueryParam.setRequestMemberType(RequestOpeTypeEnum.审批.getCode());
//        requestQueryParam.setRequestMemberStatus(RequestMemberVerifyStatusEnum.未审核.getCode());
        List<Integer> requestMemberStatusList = new ArrayList<>();
        requestMemberStatusList.add(RequestMemberVerifyStatusEnum.未审核.getCode());
        requestQueryParam.setRequestMemberStatusList(requestMemberStatusList);
        List<Integer> requestStatusList = new ArrayList<>();
        requestStatusList.add(RequestStatusEnum.审核中.getCode());
        requestStatusList.add(RequestStatusEnum.审核通过.getCode());
        requestStatusList.add(RequestStatusEnum.未通过.getCode());
        requestQueryParam.setRequestStatusList(requestStatusList);
        requestQueryParam.setEndTime(new Date());
        int waitRequestVerifyCount = requestService.getCount(requestQueryParam);
        mainPage1VO.setWaitRequestVerifyCount(waitRequestVerifyCount);
        //待我事项表决数量
        VoteQueryParam voteQueryParam = new VoteQueryParam();
        voteQueryParam.setZoneId(userEntity.getZoneId());
        voteQueryParam.setParticipateUid(uid);
        voteQueryParam.setVoteStatusList(Arrays.asList(0, 1, 2, 4));
        voteQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.待操作.getCode());
        voteQueryParam.setVoteMemberType(VoteMemberTypeEnum.表决.getCode());
        int waitVoteVerifyCount = voteService.getCount(voteQueryParam);
        mainPage1VO.setWaitVoteVerifyCount(waitVoteVerifyCount);
        //网上发函数量
        PaperQueryParam paperQueryParam = new PaperQueryParam();
        paperQueryParam.setZoneId(userEntity.getZoneId());
        paperQueryParam.setPaperStatus(PaperStatusEnum.未签收.getCode());
        paperQueryParam.setReceiverUid(uid);
        paperQueryParam.setPaperMemberStatus(PaperStatusEnum.未签收.getCode());
        int waitPaperVerifyCount = paperService.getCount(paperQueryParam);
        mainPage1VO.setWaitPaperVerifyCount(waitPaperVerifyCount);
        //费用报销数量
        BillQueryParam billQueryParam = new BillQueryParam();
        billQueryParam.setZoneId(userEntity.getZoneId());
        List<Integer> billStatusList = new ArrayList<>();
        billStatusList.add(BillStatusEnum.等待.getCode());
        billStatusList.add(BillStatusEnum.通过.getCode());
        billStatusList.add(BillStatusEnum.未通过.getCode());
        billQueryParam.setVerifyUid(uid);
        billQueryParam.setBillMemberStatus(BillMemberStatusEnum.未审批.getCode());
        int waitBillVerifyCount = billService.getCount(billQueryParam);
        mainPage1VO.setWaitBillVerifyCount(waitBillVerifyCount);

        //未开的但是我参加的会议数量
        MeetingQueryParam meetingQueryParam = new MeetingQueryParam();
        meetingQueryParam.setZoneId(userEntity.getZoneId());
        meetingQueryParam.setMeetingStatus(MeetingStatusEnum.未开始.getCode());
//        meetingQueryParam.setMeetingMemberStatus(MeetingMemberStatusEnum.待开会.getCode());
        meetingQueryParam.setParticipateMeetingUid(uid);
        int waitJoinMeetingCount = meetingService.getCount(meetingQueryParam);
        mainPage1VO.setWaitJoinMeetingCount(waitJoinMeetingCount);
        //未读通知
        NoticeQueryParam noticeQueryParam = new NoticeQueryParam();
        noticeQueryParam.setZoneId(userEntity.getZoneId());
        noticeQueryParam.setNoticeMemberStatus(1);
        noticeQueryParam.setReadUid(uid);
        int unreadNoticeCount = noticeService.getCount(noticeQueryParam);
        mainPage1VO.setUnreadNoticeCount(unreadNoticeCount);

        AnnounceQueryParam announceQueryParam = new AnnounceQueryParam();
        announceQueryParam.setStatus(AnnounceStatusEnum.公示中.getCode());
        announceQueryParam.setNoticeTime(new Date());
        announceQueryParam.setEndTime(new Date());
        announceQueryParam.setReceiverUid(uid);
        int noticeAnnounceCount = announceService.simpleList(announceQueryParam).size();
        mainPage1VO.setNoticedAnnounce(noticeAnnounceCount);


        int totalUndo = mainPage1VO.getUnreadNoticeCount()
                + mainPage1VO.getWaitBillVerifyCount()
                + mainPage1VO.getWaitJoinMeetingCount()
                + mainPage1VO.getWaitPaperVerifyCount()
                + mainPage1VO.getWaitRequestVerifyCount()
                + mainPage1VO.getWaitVoteVerifyCount();
//                + mainPage1VO.getNoticedAnnounce();
        mainPage1VO.setTotalUndo(totalUndo);
        return mainPage1VO;
    }
}
