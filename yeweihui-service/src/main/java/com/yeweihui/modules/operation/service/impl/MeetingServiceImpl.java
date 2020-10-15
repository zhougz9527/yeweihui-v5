package com.yeweihui.modules.operation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.common.utils.StrUtils;
import com.yeweihui.common.validator.Assert;
import com.yeweihui.modules.enums.meeting.MeetingMemberStatusEnum;
import com.yeweihui.modules.enums.meeting.MeetingMemberTypeEnum;
import com.yeweihui.modules.enums.meeting.MeetingStatusEnum;
import com.yeweihui.modules.operation.dao.MeetingDao;
import com.yeweihui.modules.operation.entity.MeetingEntity;
import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import com.yeweihui.modules.operation.entity.MeetingMemberEntity;
import com.yeweihui.modules.operation.service.MeetingLogService;
import com.yeweihui.modules.operation.service.MeetingMemberService;
import com.yeweihui.modules.operation.service.MeetingService;
import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.entity.SysUserRoleEntity;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.MpUserEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.MpUserService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.api.form.meeting.MeetingCancelForm;
import com.yeweihui.modules.vo.api.form.meeting.MeetingEndSignForm;
import com.yeweihui.modules.vo.api.form.meeting.MeetingSignForm;
import com.yeweihui.modules.vo.api.form.meeting.MeetingSignInForm;
import com.yeweihui.modules.vo.query.MeetingLogQueryParam;
import com.yeweihui.modules.vo.query.MeetingMemberQueryParam;
import com.yeweihui.modules.vo.query.MeetingQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service("meetingService")
public class MeetingServiceImpl extends ServiceImpl<MeetingDao, MeetingEntity> implements MeetingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    UserService userService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    MeetingMemberService meetingMemberService;

    @Autowired
    MeetingLogService meetingLogService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    MpUserService mpUserService;

    @Autowired
    ZonesService zonesService;


    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        /*Page<MeetingEntity> page = this.selectPage(
                new Query<MeetingEntity>(params).getPage(),
                new EntityWrapper<MeetingEntity>()
        );*/
        Long viewUid = (Long) params.get("viewUid");
        if (viewUid == null) {
            viewUid = ShiroUtils.getUserId();
        }
        int minRecordStatus = 2;
        if (viewUid == Constant.SUPER_ADMIN) {
            minRecordStatus = 0;
        }
        List<SysUserRoleEntity> sysUserRoleEntityList = sysUserRoleService.selectList(new EntityWrapper<SysUserRoleEntity>()
                .eq("user_id", viewUid));
        for (SysUserRoleEntity sysUserRoleEntity: sysUserRoleEntityList) {
            SysRoleEntity sysRoleEntity = sysRoleService.selectById(sysUserRoleEntity.getRoleId());
            if (sysRoleEntity.getRemark().equals("1000") || sysRoleEntity.getRemark().startsWith("13")) {
                minRecordStatus = 1;
            }
        }

        params.put("minRecordStatus", minRecordStatus);
        Page page = new Query(params).getPage();
        List<MeetingEntity> list = this.baseMapper.selectPageEn(page, params);
        for (MeetingEntity meeting :list) {
            this.packageExtraInfo(meeting, false, viewUid);
        }

        /*for(MeetingEntity u : list){
            System.out.println("去重前的数据" + StrUtils.toJson(u));
        }*/

        //memberUid 和 mid去重
        /*Set<MeetingEntity> set = new TreeSet<>(new Comparator<MeetingEntity>() {
            @Override
            public int compare(MeetingEntity meetingEntity1, MeetingEntity meetingEntity2) {
                int count = 1;
                if(meetingEntity1.getMemberUid().equals(meetingEntity2.getMemberUid()) &&
                        meetingEntity1.getId().equals(meetingEntity2.getId())){
                    count = 0;
                }
                return count;
            }
        });
        set.addAll(list);*/
        /*for(MeetingEntity u : set){
            System.out.println("去重后的数据" + StrUtils.toJson(u));
        }*/

        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    public MeetingEntity info(Long id, UserEntity userEntity) {
        MeetingEntity meeting = this.selectById(id);
        this.packageExtraInfo(meeting, true, userEntity == null ? null : userEntity.getId());
        return meeting;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(MeetingEntity meeting) {
        UserEntity userEntity = userService.selectById(meeting.getUid());
        if (null != userEntity){
            meeting.setZoneId(userEntity.getZoneId());
        }

        //会议参与成员
        List<MeetingMemberEntity> meetingMemberEntityList = meeting.getMeetingMemberEntityList();
        if (meetingMemberEntityList == null || meetingMemberEntityList.size() == 0){
            throw new RRException("请传会议参与成员");
        }

        boolean creatorInMember = false;
        for (MeetingMemberEntity meetingMemberEntity : meetingMemberEntityList) {
            if (meeting.getUid().equals(meetingMemberEntity.getUid())) {
                creatorInMember = true;
                break;
            }
        }

        if (!creatorInMember) {
            throw new RRException("请将创建人列为参会人员!");
        }

        meeting.setNum(meetingMemberEntityList.size());
        if (meeting.getType() == 1) {
            // 现场会议直接就是进行中的状态
            meeting.setStatus(MeetingStatusEnum.进行中.getCode());
        }
        this.insert(meeting);

        for (MeetingMemberEntity meetingMemberEntity : meetingMemberEntityList) {
            meetingMemberEntity.setMid(meeting.getId());
            meetingMemberEntity.setReferUid(meeting.getUid());
            meetingMemberEntity.setType(MeetingMemberTypeEnum.参会.getCode());
            meetingMemberEntity.setStatus(MeetingMemberStatusEnum.待开会.getCode());
        }
        meetingMemberService.insertBatch(meetingMemberEntityList);
        //会议抄送成员
        List<MeetingMemberEntity> copy2MeetingMemberEntityList = meeting.getCopy2MeetingMemberEntityList();
        if (copy2MeetingMemberEntityList != null && copy2MeetingMemberEntityList.size() > 0){
            for (MeetingMemberEntity meetingMemberEntity : copy2MeetingMemberEntityList) {
                meetingMemberEntity.setMid(meeting.getId());
                meetingMemberEntity.setReferUid(meeting.getUid());
                meetingMemberEntity.setType(MeetingMemberTypeEnum.抄送.getCode());
//            meetingMemberEntity.setStatus(MeetingMemberStatusEnum.待开会.getCode());
            }
            meetingMemberService.insertBatch(copy2MeetingMemberEntityList);
        }

        Long mid = meeting.getId();

        new Timer().schedule(new TimerTask() {

            int i=0;
            public void run() {

                i = i+1;
                if (i>=4)return;

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                for (MeetingMemberEntity meetingMemberEntity: meetingMemberEntityList) {
                    MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", meetingMemberEntity.getUid()));
                    if (mpUserEntity == null) continue;
                    String openId = mpUserEntity.getOpenidPublic();
                    JSONObject meetingRemind = new JSONObject();
                    meetingRemind.put("touser", openId);
                    meetingRemind.put("template_id", "IVbRiTdn7YqjGtzMiijEaVTfOdKMAAtDpdrLNaY7TyU");
                    JSONObject miniProgram = new JSONObject();
                    miniProgram.put("appid", "wx5e7524c02dade60a");
                    miniProgram.put("pagepath", String.format("pages/meetingdetail/index?id=%s", mid));
                    meetingRemind.put("miniprogram", miniProgram);
                    JSONObject data = new JSONObject();
                    JSONObject first = new JSONObject();
                    first.put("value", "您有一个新的会议提醒");
                    JSONObject keyword1 = new JSONObject();
                    keyword1.put("value", meeting.getTitle());
                    JSONObject keyword2 = new JSONObject();
                    keyword2.put("value", df.format(meeting.getStartAt()));
                    data.put("first", first);
                    data.put("keyword1", keyword1);
                    data.put("keyword2", keyword2);
                    meetingRemind.put("data", data);
                    mpUserService.pushTemplateMessage(meetingRemind, meetingMemberEntity.getUid(), "meeting");
                }

                // 推送消息给抄送人
                if (copy2MeetingMemberEntityList == null) {
                    return;
                }
                for (MeetingMemberEntity meetingMemberEntity: copy2MeetingMemberEntityList) {
                    MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", meetingMemberEntity.getUid()));
                    if (mpUserEntity == null) continue;
                    String openId = mpUserEntity.getOpenidPublic();
                    JSONObject meetingCopyRemind = new JSONObject();
                    meetingCopyRemind.put("touser", openId);
                    meetingCopyRemind.put("template_id", "IVbRiTdn7YqjGtzMiijEaVTfOdKMAAtDpdrLNaY7TyU");
                    JSONObject miniProgram = new JSONObject();
                    miniProgram.put("appid", "wx5e7524c02dade60a");
                    miniProgram.put("pagepath", String.format("pages/meetingdetail/index?id=%s", mid));
                    meetingCopyRemind.put("miniprogram", miniProgram);
                    JSONObject data = new JSONObject();
                    JSONObject first = new JSONObject();
                    first.put("value", "您有一份会议抄送");
                    JSONObject keyword1 = new JSONObject();
                    keyword1.put("value", meeting.getTitle());
                    JSONObject keyword2 = new JSONObject();
                    keyword2.put("value", df.format(meeting.getStartAt()));
                    data.put("first", first);
                    data.put("keyword1", keyword1);
                    data.put("keyword2", keyword2);
                    meetingCopyRemind.put("data", data);
                    mpUserService.pushTemplateMessage(meetingCopyRemind, meetingMemberEntity.getUid(), "meeting");
                }

            }
        }, 1000*60*60*12 , 1000);


        /*//获取当前角色组的角色roleCodeList
        List<String> roleCodeList = sysRoleService.getRoleCodeByGroup("业主委员会");
        //根据小区id和角色列表获取用户人员列表
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setZoneId(meeting.getZoneId());
        userQueryParam.setRoleCodeList(roleCodeList);
        List<UserEntity> userEntityList = userService.simpleList(userQueryParam);
        //需要参加会议的总人数是业主委员会的全体成员
        meeting.setNum(userEntityList.size());
        for (int i = 0; i < userEntityList.size(); i++) {
            UserEntity userEntity1 = userEntityList.get(i);
            MeetingMemberEntity meetingMemberEntity = new MeetingMemberEntity();
            meetingMemberEntity.setMid(meeting.getId());
            meetingMemberEntity.setUid(userEntity1.getId());
            meetingMemberEntity.setReferUid(meeting.getUid());
            meetingMemberEntity.setType(MeetingMemberTypeEnum.参会.getCode());//未涉及到抄送的
            meetingMemberEntity.setStatus(MeetingMemberVerifyStatusEnum.未审核.getCode());
            meetingMemberService.insert(meetingMemberEntity);
        }*/

    }

    @Override
    @Transactional
    public void update(MeetingEntity meeting) {
        this.updateAllColumnById(meeting);//全部更新
    }

    @Override
    public MeetingEntity infoDetail(Long id) {
        MeetingEntity meetingEntity = this.selectById(id);
        MeetingMemberQueryParam meetingMemberQueryParam = new MeetingMemberQueryParam();
        meetingMemberQueryParam.setMid(id);
        meetingMemberQueryParam.setMeetingMemberType(MeetingMemberTypeEnum.参会.getCode());
        //会议参与成员
        List<MeetingMemberEntity> meetingMemberEntityList = meetingMemberService.simpleList(meetingMemberQueryParam);
        meetingEntity.setMeetingMemberEntityList(meetingMemberEntityList);

        //会议抄送成员
        meetingMemberQueryParam.setMeetingMemberType(MeetingMemberTypeEnum.抄送.getCode());
        List<MeetingMemberEntity> copy2MeetingMemberEntityList = meetingMemberService.simpleList(meetingMemberQueryParam);
        meetingEntity.setCopy2MeetingMemberEntityList(copy2MeetingMemberEntityList);

        //会议日志
        MeetingLogQueryParam meetingLogQueryParam = new MeetingLogQueryParam();
        meetingLogQueryParam.setMid(id);
        List<MeetingLogEntity> meetingLogEntityList = meetingLogService.simpleList(meetingLogQueryParam);
        meetingEntity.setMeetingLogEntityList(meetingLogEntityList);
        return meetingEntity;
    }

    /**
     * 会议签到
     * @param meetingSignInForm
     */
    @Override
    @Transactional
    public void meetingSignIn(MeetingSignInForm meetingSignInForm) {
        MeetingEntity meetingEntity = this.selectById(meetingSignInForm.getMid());
        Assert.isNull(meetingEntity, "当前会议不存在");
        //会议开始进行中可以签到
        if (meetingEntity.getStatus().equals(MeetingStatusEnum.进行中.getCode())
                || meetingEntity.getStatus().equals(MeetingStatusEnum.待签字.getCode())){
            MeetingMemberEntity meetingMemberEntity = meetingMemberService.getByMidUid(
                    meetingSignInForm.getMid(), meetingSignInForm.getUid(), MeetingMemberTypeEnum.参会.getCode());
            Assert.isNull(meetingMemberEntity, "当前用户未批准参会");
            if (!meetingMemberEntity.getStatus().equals(MeetingMemberStatusEnum.待开会.getCode())){
                throw new RRException("当前会议参与状态不能签到");
            }else{
                meetingMemberEntity.setStatus(MeetingMemberStatusEnum.已签到.getCode());
                meetingMemberEntity.setSignTime(new Date());
                meetingMemberEntity.setSignInUrl(meetingSignInForm.getVerifyUrl());
                meetingMemberService.updateById(meetingMemberEntity);
                //签到人数+1
                meetingEntity.setSignNum(meetingEntity.getSignNum()+1);
                /*if (meetingEntity.getNum().equals(meetingEntity.getSignNum())){
                    meetingEntity.setStatus(MeetingStatusEnum.进行中.getCode());
                }*/
                this.updateById(meetingEntity);
            }
        }else{
            throw new RRException("当前会议状态["+ MeetingStatusEnum.getName(meetingEntity.getStatus()) +"]不能签到");
        }
    }

    /**
     * 会议撤销
     * @param meetingCancelForm
     */
    @Override
    @Transactional
    public void meetingCancel(MeetingCancelForm meetingCancelForm) {
        MeetingEntity meetingEntity = this.selectById(meetingCancelForm.getMid());
        Assert.isNull(meetingEntity, "当前会议不存在");
        if (!meetingEntity.getUid().equals(meetingCancelForm.getUid())){
            throw new RRException("当前用户非会议发起用户，不能进行撤销");
        }
        if (!meetingEntity.getStatus().equals(MeetingStatusEnum.未开始.getCode())){
            throw new RRException("当前会议状态不能撤销");
        }
        meetingEntity.setStatus(MeetingStatusEnum.取消.getCode());
        this.updateById(meetingEntity);
    }

    /**
     * 会议结束签字
     * @param meetingEndSignForm
     */
    @Override
    @Transactional
    public void meetingEndSign(MeetingEndSignForm meetingEndSignForm) {
        MeetingEntity meetingEntity = this.selectById(meetingEndSignForm.getMid());
        Assert.isNull(meetingEntity, "当前会议不存在");
        if (!meetingEntity.getStatus().equals(MeetingStatusEnum.进行中.getCode())){
            throw new RRException("当前会议状态"+ MeetingStatusEnum.getName(meetingEntity.getStatus()) +"不能会议结束签字");
        }
        if (!meetingEntity.getUid().equals(meetingEndSignForm.getUid())){
            throw new RRException("当前用户不是会议发起者，不能结束会议");
        }
        MeetingMemberEntity meetingMemberEntity = meetingMemberService.getByMidUid(
                meetingEndSignForm.getMid(), meetingEndSignForm.getUid(), MeetingMemberTypeEnum.参会.getCode());
        Assert.isNull(meetingMemberEntity, "当前用户未批准参会");
        // 预约开会，必须签到才能结束
        if (meetingEntity.getType() == 0 && !meetingMemberEntity.getStatus().equals(MeetingMemberStatusEnum.已签到.getCode())){
            throw new RRException("当前会议参与状态"+ MeetingMemberStatusEnum.getName(meetingMemberEntity.getStatus()) +"不能会议结束签字");
        }else{
//            meetingMemberEntity.setStatus(MeetingMemberStatusEnum.已签字.getCode());
//            meetingMemberEntity.setSignNameTime(new Date());
//            meetingMemberService.updateById(meetingMemberEntity);
        }
        meetingEntity.setEndDate(new Date());
        meetingEntity.setStatus(MeetingStatusEnum.待签字.getCode());
        this.updateById(meetingEntity);
    }

    /**
     * 参加的会议数量
     * @param meetingQueryParam
     * @return
     */
    @Override
    public int getCount(MeetingQueryParam meetingQueryParam) {
        return this.baseMapper.getCount(meetingQueryParam);
    }

    /**
     * 会议时间到，会议状态待签到变为进行中
     */
    @Override
    public void change2Running() {
        MeetingQueryParam meetingQueryParam = new MeetingQueryParam();
        meetingQueryParam.setAfterStartTime("true");
        meetingQueryParam.setMeetingStatus(MeetingStatusEnum.未开始.getCode());
        //会议为未开始状态，时间已经为开始
        List<MeetingEntity> meetingEntityList = this.simpleList(meetingQueryParam);
        logger.info("change2Running meetingEntityList:{}", StrUtils.toJson(meetingEntityList));
        if (null != meetingEntityList && meetingEntityList.size()>0){
            for (MeetingEntity meetingEntity : meetingEntityList) {
                meetingEntity.setStatus(MeetingStatusEnum.进行中.getCode());
            }
            this.updateBatchById(meetingEntityList);
        }
    }

    @Override
    public void expireMeeting() {
        MeetingQueryParam meetingQueryParam = new MeetingQueryParam();
        meetingQueryParam.setExpireTime(new Date());
        List<MeetingEntity> meetingEntityList = this.simpleList(meetingQueryParam);
        if (null == meetingEntityList || meetingEntityList.size() == 0) {
            return;
        }
        for (MeetingEntity meetingEntity: meetingEntityList) {
            List<MeetingMemberEntity> meetingMemberEntityList = meetingMemberService.selectList(
                    new EntityWrapper<MeetingMemberEntity>()
                            .eq("type", MeetingMemberTypeEnum.参会.getCode())
                            .ne("status", MeetingMemberStatusEnum.已签字)
            );
            if (null == meetingMemberEntityList || meetingMemberEntityList.size() == 0) {
                continue;
            }
            logger.info("获取出超过48小时，未签字的会议委员状态需要变成签字同意的操作记录:{}", StrUtils.toJson(meetingMemberEntityList));
            for (MeetingMemberEntity meetingMemberEntity: meetingMemberEntityList) {
                meetingMemberEntity.setStatus(MeetingMemberStatusEnum.已签字.getCode());
            }
            meetingMemberService.updateBatchById(meetingMemberEntityList);
            innerChangeStatus(meetingEntity);
        }
    }

    @Override
    public List<MeetingEntity> simpleList(MeetingQueryParam meetingQueryParam) {
        return this.baseMapper.simpleList(meetingQueryParam);
    }

    /**
     * 会议签字
     * @param meetingSignForm
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void meetingSign(MeetingSignForm meetingSignForm) {
        MeetingEntity meetingEntity = this.selectById(meetingSignForm.getMid());
        Assert.isNull(meetingEntity, "当前会议不存在");
        if (!meetingEntity.getStatus().equals(MeetingStatusEnum.待签字.getCode())){
            throw new RRException("当前会议参与状态"+ MeetingStatusEnum.getName(meetingEntity.getStatus()) +"不能会议结束签字");
        }
        MeetingMemberEntity meetingMemberEntity = meetingMemberService.getByMidUid(
                meetingSignForm.getMid(), meetingSignForm.getUid(), MeetingMemberTypeEnum.参会.getCode());
        Assert.isNull(meetingMemberEntity, "当前用户未批准参会");
        if (meetingEntity.getType() == 0 && !meetingMemberEntity.getStatus().equals(MeetingMemberStatusEnum.已签到.getCode())){
            throw new RRException("当前会议参与状态"+ MeetingMemberStatusEnum.getName(meetingMemberEntity.getStatus()) +"不能会议结束签字");
        }else{
            meetingMemberEntity.setStatus(MeetingMemberStatusEnum.已签字.getCode());
            meetingMemberEntity.setSignNameTime(new Date());
            meetingMemberEntity.setSignEndUrl(meetingSignForm.getSignEndUrl());
            meetingMemberService.updateById(meetingMemberEntity);
        }
        innerChangeStatus(meetingEntity);
    }

    private void innerChangeStatus(MeetingEntity meetingEntity) {
        MeetingMemberQueryParam meetingMemberQueryParam = new MeetingMemberQueryParam();
        meetingMemberQueryParam.setMid(meetingEntity.getId());
        meetingMemberQueryParam.setMeetingMemberType(MeetingMemberTypeEnum.参会.getCode());
        meetingMemberQueryParam.setSignFlag("true");
        //参会且已签到的人数
        int totalMeetingMemberCount = meetingMemberService.getCount(meetingMemberQueryParam);
        meetingMemberQueryParam.setMeetingMemberStatus(MeetingMemberStatusEnum.已签字.getCode());
        int signedNameMeetingMemberCount = meetingMemberService.getCount(meetingMemberQueryParam);
        logger.info("meetingSign totalMeetingMemberCount:{}, signedNameMeetingMemberCount:{}",
                totalMeetingMemberCount, signedNameMeetingMemberCount);
        if (totalMeetingMemberCount == signedNameMeetingMemberCount){
            //如果所有人都签字了则会议结束
            meetingEntity.setStatus(MeetingStatusEnum.结束.getCode());
            this.updateById(meetingEntity);
        }

    }

    private void packageExtraInfo(MeetingEntity meetingEntity, boolean isInfo, Long uid){
        if (isInfo){
            MeetingMemberQueryParam meetingMemberQueryParam = new MeetingMemberQueryParam();
            meetingMemberQueryParam.setMid(meetingEntity.getId());
            meetingMemberQueryParam.setMeetingMemberType(MeetingMemberTypeEnum.参会.getCode());
            List<MeetingMemberEntity> meetingMemberEntityList = meetingMemberService.simpleList(meetingMemberQueryParam);
            for (MeetingMemberEntity meetingMemberEntity : meetingMemberEntityList) {
                meetingMemberEntity.setStatusCn(MeetingMemberStatusEnum.getName(meetingMemberEntity.getStatus()));
            }
            //参会人员
            meetingEntity.setMeetingMemberEntityList(meetingMemberEntityList);

            meetingMemberQueryParam.setMeetingMemberType(MeetingMemberTypeEnum.抄送.getCode());
            List<MeetingMemberEntity> copy2MeetingMemberEntityList = meetingMemberService.simpleList(meetingMemberQueryParam);
            //抄送人员
            meetingEntity.setCopy2MeetingMemberEntityList(copy2MeetingMemberEntityList);

            //会议纪要
            MeetingLogQueryParam meetingLogQueryParam = new MeetingLogQueryParam();
            meetingLogQueryParam.setMid(meetingEntity.getId());
            List<MeetingLogEntity> meetingLogEntityList = meetingLogService.simpleList(meetingLogQueryParam);
            //会议日志
            meetingEntity.setMeetingLogEntityList(meetingLogEntityList);
            //打印日期
            meetingEntity.setPrintDate(new Date());
        }

        meetingEntity.setStatusCn(MeetingStatusEnum.getName(meetingEntity.getStatus()));

        if (uid != null){
            MeetingMemberEntity meetingMemberEntity = meetingMemberService.getByMidUid(meetingEntity.getId(), uid, MeetingMemberTypeEnum.参会.getCode());
            if (null != meetingMemberEntity){
                meetingEntity.setMemberStatus(meetingMemberEntity.getStatus());
                meetingEntity.setMemberStatusCn(MeetingMemberStatusEnum.getName(meetingMemberEntity.getStatus()));
                meetingEntity.setVerifyUrl(meetingMemberEntity.getSignInUrl());
            }
        }

        UserEntity userEntity = userService.selectById(meetingEntity.getUid());
        if (null != userEntity){
            meetingEntity.setAvatarUrl(userEntity.getAvatarUrl());
            meetingEntity.setRealname(userEntity.getRealname());
        }

        if (meetingEntity.getZoneId()!=null){
            ZonesEntity zonesEntity = zonesService.selectById(meetingEntity.getZoneId());
            if (null != zonesEntity){
                meetingEntity.setZoneName(zonesEntity.getName());
            }
        }

    }

}
