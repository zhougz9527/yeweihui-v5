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
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.enums.request.RequestStatusEnum;
import com.yeweihui.modules.enums.vote.*;
import com.yeweihui.modules.operation.dao.VoteDao;
import com.yeweihui.modules.operation.entity.VoteEntity;
import com.yeweihui.modules.operation.entity.VoteMemberEntity;
import com.yeweihui.modules.operation.service.VoteMemberService;
import com.yeweihui.modules.operation.service.VoteService;
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
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.api.form.VoteForm;
import com.yeweihui.modules.vo.api.form.vote.VoteCancelForm;
import com.yeweihui.modules.vo.query.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("voteService")
public class VoteServiceImpl extends ServiceImpl<VoteDao, VoteEntity> implements VoteService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    MultimediaService multimediaService;

    @Autowired
    VoteMemberService voteMemberService;

    @Autowired
    ZonesService zonesService;

    @Autowired
    MpUserService mpUserService;

    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        /*Page<VoteEntity> page = this.selectPage(
                new Query<VoteEntity>(params).getPage(),
                new EntityWrapper<VoteEntity>()
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
        List<VoteEntity> list = this.baseMapper.selectPageEn(page, params);
        for (VoteEntity vote : list) {
            this.packageExtra(vote, false, viewUid);
        }
        page.setRecords(list);

        return new PageUtils(page);
    }

    /**
     * 保存
     * @param vote
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(VoteEntity vote) {
        if (vote.getVerifyType() == 1 && vote.getVoteMemberEntityList().size() !=1) {
            throw new RRException("单人审批只能有一个审批人");
        }
        UserEntity userEntity = userService.selectById(vote.getUid());
        if (null != userEntity){
            vote.setZoneId(userEntity.getZoneId());
        }
        //添加时间
        vote.setCreateAt(new Date());
        List<UserEntity> userEntityList = null;

        List<VoteMemberEntity> voteMemberEntityList = vote.getVoteMemberEntityList();
        //如果前端没有传值则是全体业委会成员
        if (null != voteMemberEntityList && voteMemberEntityList.size() > 0){
            vote.setNum(voteMemberEntityList.size());
        }else{
            //获取当前角色组的角色roleCodeList
            List<String> roleCodeList = sysRoleService.getRoleCodeByGroup("业主委员会");
            //根据小区id和角色列表获取用户人员列表
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setZoneId(vote.getZoneId());
            userQueryParam.setRoleCodeList(roleCodeList);
            userEntityList = userService.simpleList(userQueryParam);
            //需要审批的总人数是业主委员会的全体成员
            vote.setNum(userEntityList.size());

            //如果前端没有传值则是全体业委会成员
            for (int i = 0; i < userEntityList.size(); i++) {
                UserEntity userEntity1 = userEntityList.get(i);
                VoteMemberEntity voteMemberEntity = new VoteMemberEntity();
                voteMemberEntity.setVid(vote.getId());
                voteMemberEntity.setUid(userEntity1.getId());
                voteMemberEntity.setReferUid(vote.getUid());
                voteMemberEntity.setType(VoteMemberTypeEnum.表决.getCode());//未涉及到抄送的
                voteMemberEntity.setStatus(VoteMemberStatusEnum.待操作.getCode());
                voteMemberService.insert(voteMemberEntity);
            }
        }

        this.insert(vote);

        if (null != voteMemberEntityList && voteMemberEntityList.size() > 0){
            //投票人员前端传值不为空
            for (VoteMemberEntity voteMemberEntity : voteMemberEntityList) {
                voteMemberEntity.setVid(vote.getId());
//                voteMemberEntity.setUid(userEntity1.getId());
                voteMemberEntity.setReferUid(vote.getUid());
                voteMemberEntity.setType(VoteMemberTypeEnum.表决.getCode());//未涉及到抄送的
                voteMemberEntity.setStatus(VoteMemberStatusEnum.待操作.getCode());
                voteMemberService.insert(voteMemberEntity);
            }
        }else{
            //如果前端没有传值则是全体业委会成员
            for (int i = 0; i < userEntityList.size(); i++) {
                UserEntity userEntity1 = userEntityList.get(i);
                VoteMemberEntity voteMemberEntity = new VoteMemberEntity();
                voteMemberEntity.setVid(vote.getId());
                voteMemberEntity.setUid(userEntity1.getId());
                voteMemberEntity.setReferUid(vote.getUid());
                voteMemberEntity.setType(VoteMemberTypeEnum.表决.getCode());//未涉及到抄送的
                voteMemberEntity.setStatus(VoteMemberStatusEnum.待操作.getCode());
                voteMemberEntityList.add(voteMemberEntity);
                voteMemberService.insert(voteMemberEntity);
            }
        }

        List<VoteMemberEntity> copy2VoteMemberEntityList = vote.getCopy2VoteMemberEntityList();
        if(copy2VoteMemberEntityList != null && copy2VoteMemberEntityList.size()>0){
            for (VoteMemberEntity voteMemberEntity : copy2VoteMemberEntityList) {
                voteMemberEntity.setVid(vote.getId());
//            voteMemberEntity.setUid();
                voteMemberEntity.setReferUid(vote.getUid());
                voteMemberEntity.setType(VoteMemberTypeEnum.抄送.getCode());//未涉及到抄送的
//            voteMemberEntity.setStatus(VoteMemberStatusEnum.待操作.getCode());
                voteMemberService.insert(voteMemberEntity);
            }
        }
        this.saveOrUpdateExtra(vote);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        UserEntity refUser = userService.selectById(vote.getUid());
        ZonesEntity refZone = zonesService.selectById(vote.getZoneId());
        Long vid = vote.getId();
//        Long vid = 232L; //测试

        for (VoteMemberEntity voteMemberEntity: voteMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", voteMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject voteRemind = new JSONObject();
            voteRemind.put("touser", openId);
            voteRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/votedetail/index?id=%s", vid));
            voteRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一项新的事项表决");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "事项表决");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", refUser.getRealname());
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(vote.getCreateTime()));
            JSONObject keyword4 = new JSONObject();
            keyword4.put("value", userEntity.getRoleName());
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("keyword4", keyword4);
            voteRemind.put("data", data);
            System.out.println(voteRemind);
            mpUserService.pushTemplateMessage(voteRemind, voteMemberEntity.getUid(), "vote");
        }

        // 推送消息给抄送人
        for (VoteMemberEntity voteMemberEntity: copy2VoteMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", voteMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject voteCopyRemind = new JSONObject();
            voteCopyRemind.put("touser", openId);
            voteCopyRemind.put("template_id", "IkDeKDMaVCgGpq52Pfju9WwjYc66qPbMDbHA39u3qMs");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/votedetail/index?id=%s", vid));
            voteCopyRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一份表决抄送");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "事项表决");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "新发起");
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(vote.getCreateTime()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            voteCopyRemind.put("data", data);
            mpUserService.pushTemplateMessage(voteCopyRemind, voteMemberEntity.getUid(), "vote");
        }

    }

    @Override
    public VoteEntity info(Long id, UserEntity userEntity) {
        VoteEntity vote = this.selectById(id);
        if (userEntity != null) {
            this.packageExtra(vote, true, userEntity.getId());
        } else {
            this.packageExtra(vote, true, null);
        }
        return vote;
    }

    @Override
    @Transactional
    public void update(VoteEntity vote) {
        this.updateAllColumnById(vote);//全部更新
        this.saveOrUpdateExtra(vote);
    }

    @Override
    public int getCount(VoteQueryParam voteQueryParam) {
        return this.baseMapper.getCount(voteQueryParam);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void vote(VoteForm voteForm) {
        logger.info("vote voteForm:{}", StrUtils.toJson(voteForm));

        VoteEntity voteEntity = this.selectById(voteForm.getVid());
        Assert.isNull(voteEntity, "当前表决不存在");
        VoteMemberEntity voteMemberEntity = voteMemberService.getByVidUid(voteForm.getVid(), voteForm.getUid(), VoteMemberTypeEnum.表决.getCode());
        if (null == voteMemberEntity){
            throw new RRException("当前用户没有无法参与当前表决的权限");
        }
        if (voteEntity.getItemType().equals(VoteItemTypeEnum.单项.getCode())){
            voteMemberEntity.setStatus(voteForm.getStatus());
        }else if (voteEntity.getItemType().equals(VoteItemTypeEnum.多选.getCode())){
            voteMemberEntity.setItem1(voteForm.getItem1());
            voteMemberEntity.setItem2(voteForm.getItem2());
            voteMemberEntity.setItem3(voteForm.getItem3());
            voteMemberEntity.setItem4(voteForm.getItem4());
            if (voteForm.getItem1() != null || voteForm.getItem2() != null || voteForm.getItem3() != null || voteForm.getItem4() != null){
                voteMemberEntity.setStatus(VoteMemberStatusEnum.多选已表决.getCode());
            }else{
                voteMemberEntity.setStatus(voteForm.getStatus());
            }
        }

        voteMemberEntity.setRemark(voteForm.getRemark());
        voteMemberEntity.setVerifyUrl(voteForm.getVerifyUrl());
        voteMemberService.updateById(voteMemberEntity);
        //数量判断，更新表决状态
        this.innerChangeVoteStatus(voteEntity);


       /* if(voteEntity.getStatus().equals(VoteStatusEnum.进行中.getCode())){

        }else{
            throw new RRException("当前表决状态不能表决");
        }*/
    }

    /**
     * 获取出投票未完成的，超过投票截止时间的，默认审核弃权；并更新表决状态
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void expireVote() {
        VoteQueryParam voteQueryParam = new VoteQueryParam();
//        voteQueryParam.setVoteStatus(VoteStatusEnum.进行中.getCode());
        voteQueryParam.setExpire("true");
        voteQueryParam.setVoteStatus(VoteStatusEnum.进行中.getCode());
        List<VoteEntity> voteEntityList = this.simpleList(voteQueryParam);
        for (VoteEntity voteEntity : voteEntityList) {
            VoteMemberQueryParam voteMemberQueryParam = new VoteMemberQueryParam();
            voteMemberQueryParam.setVid(voteEntity.getId());
            voteMemberQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.待操作.getCode());
            List<VoteMemberEntity> voteMemberEntityList = voteMemberService.simpleList(voteMemberQueryParam);
            if (null != voteMemberEntityList && voteMemberEntityList.size() != 0) {
                for (VoteMemberEntity voteMemberEntity : voteMemberEntityList) {
                    //超时算作弃权
                    voteMemberEntity.setStatus(VoteMemberStatusEnum.弃权.getCode());
                }
                voteMemberService.updateBatchById(voteMemberEntityList);
            }

            //数量判断，更新表决状态
            this.innerChangeVoteStatus(voteEntity);
        }

    }

    @Override
    public List<VoteEntity> simpleList(VoteQueryParam voteQueryParam) {
        return this.baseMapper.simpleList(voteQueryParam);
    }

    /**
     * 用户撤销投票
     * @param voteCancelForm
     */
    @Override
    public void voteCancel(VoteCancelForm voteCancelForm) {
        VoteEntity voteEntity = this.selectById(voteCancelForm.getVid());
        Assert.isNull(voteEntity, "当前投票不存在");
        if (!voteEntity.getUid().equals(voteCancelForm.getUid())){
            throw new RRException("非发起投票用户，不能进行撤销操作");
        }
        if (!voteEntity.getStatus().equals(VoteStatusEnum.进行中.getCode())){
            throw new RRException("当前投票状态为"+ VoteStatusEnum.getName(voteEntity.getStatus()) +"不能进行撤销");
        }
        voteEntity.setStatus(VoteStatusEnum.撤销.getCode());
        this.updateById(voteEntity);
    }

    /**
     * 数量判断，更新表决状态
     * select distinct(vote_item) from vote
     * @param voteEntity
     */
    private void innerChangeVoteStatus(VoteEntity voteEntity) {
        int beforeStatus = voteEntity.getStatus();
        VoteMemberQueryParam voteMemberQueryParam = new VoteMemberQueryParam();
        voteMemberQueryParam.setVid(voteEntity.getId());
        voteMemberQueryParam.setVoteMemberType(VoteMemberTypeEnum.表决.getCode());
        //所有的数量
        int totalCount = voteMemberService.getCount(voteMemberQueryParam);
        //单选
        if (voteEntity.getItemType().equals(VoteItemTypeEnum.单项.getCode())){
            //已通过的数量
            voteMemberQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.同意.getCode());
            int agreeCount = voteMemberService.getCount(voteMemberQueryParam);
            //反对的数量
            voteMemberQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.反对.getCode());
            int disAgreeCount = voteMemberService.getCount(voteMemberQueryParam);
            //弃权的数量
            voteMemberQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.弃权.getCode());
            int abandonCount = voteMemberService.getCount(voteMemberQueryParam);
            //超时的数量 无超时，算作弃权
//            voteMemberQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.超时.getCode());
//            int expireCount = voteMemberService.getCount(voteMemberQueryParam);

//            logger.info("innerChangeVoteStatus 单选 totalCount:{}, agreeCount:{}, disAgreeCount:{}, abandonCount:{}",
//                    totalCount, agreeCount, disAgreeCount, abandonCount);

            if (voteEntity.getVerifyType() == 2) {
                if (totalCount == agreeCount + disAgreeCount + abandonCount) {
                    voteEntity.setStatus(VoteStatusEnum.人工判断.getCode());
                    this.updateById(voteEntity);
                }
            } else if (totalCount%2!=0){//奇数
//                logger.info("agreeCount >= (totalCount+1) / 2:{}", agreeCount >= (totalCount+1) / 2);
//                logger.info("(disAgreeCount + abandonCount) >= (totalCount+1) / 2:{}", (disAgreeCount + abandonCount) >= (totalCount+1) / 2);

                if (agreeCount >= (totalCount+1) / 2){
                    //如果过半的话审批通过
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    this.updateById(voteEntity);
                }else if((disAgreeCount + abandonCount) >= (totalCount+1) / 2){
                    //如果弃权和超时的过半的话审批拒绝
                    voteEntity.setStatus(VoteStatusEnum.不通过.getCode());
                    this.updateById(voteEntity);
                }
            }else{//偶数
//                logger.info("agreeCount > totalCount / 2:{}", agreeCount > totalCount / 2);
//                logger.info("(disAgreeCount + abandonCount) >= totalCount / 2:{}", (disAgreeCount + abandonCount) >= totalCount / 2);
                if (agreeCount > totalCount / 2){
                    //如果过半的话审批通过
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    this.updateById(voteEntity);
                }else if ((disAgreeCount + abandonCount) >= totalCount / 2){
                    //如果过半的话审批拒绝
                    voteEntity.setStatus(VoteStatusEnum.不通过.getCode());
                    this.updateById(voteEntity);
                }
            }

        }else if (voteEntity.getItemType().equals(VoteItemTypeEnum.多选.getCode())){
            voteMemberQueryParam.setVoteMemberStatus(null);
            voteMemberQueryParam.setItemChoice("item1");
            int item1Count = voteMemberService.getCount(voteMemberQueryParam);
            voteMemberQueryParam.setItemChoice("item2");
            int item2Count = voteMemberService.getCount(voteMemberQueryParam);
            voteMemberQueryParam.setItemChoice("item3");
            int item3Count = voteMemberService.getCount(voteMemberQueryParam);
            voteMemberQueryParam.setItemChoice("item4");
            int item4Count = voteMemberService.getCount(voteMemberQueryParam);

//            logger.info("innerChangeVoteStatus 多选 totalCount:{}, item1Count:{}, item2Count:{}, item3Count:{}, item4Count:{}",
//                    totalCount, item1Count, item2Count, item3Count, item4Count);
            voteMemberQueryParam.setItemChoice(null);
            voteMemberQueryParam.setVoteMemberStatus(VoteMemberStatusEnum.弃权.getCode());
            int abandonCount = voteMemberService.getCount(voteMemberQueryParam);

            if (voteEntity.getVerifyType() == 2) {
                if (totalCount == item1Count + item2Count + item3Count + item4Count) {
                    voteEntity.setStatus(VoteStatusEnum.人工判断.getCode());
                    this.updateById(voteEntity);
                }
            } else if (totalCount%2!=0) {//奇数
//                logger.info("item1Count >= (totalCount+1) / 2:{}", item1Count >= (totalCount+1) / 2);
//                logger.info("item2Count >= (totalCount+1) / 2:{}", item2Count >= (totalCount+1) / 2);
//                logger.info("item3Count >= (totalCount+1) / 2:{}", item3Count >= (totalCount+1) / 2);
//                logger.info("item4Count >= (totalCount+1) / 2:{}", item4Count >= (totalCount+1) / 2);
                if (item1Count >= (totalCount+1) / 2){
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项1");
                    this.updateById(voteEntity);
                }else if (item2Count >= (totalCount+1) / 2){
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项2");
                    this.updateById(voteEntity);
                }else if (item3Count >= (totalCount+1) / 2){
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项3");
                    this.updateById(voteEntity);
                }else if (item4Count >= (totalCount+1) / 2){
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项4");
                    this.updateById(voteEntity);
                }else{
                    voteMemberQueryParam.setItemChoice(null);
                    //如果都选了而且没有一项是过半的
                    int votedCount = item1Count + item2Count + item3Count + item4Count + abandonCount;
                    if (votedCount == totalCount){
                        voteEntity.setStatus(VoteStatusEnum.不通过.getCode());
                        this.updateById(voteEntity);
                    }
                }
            }else{//偶数
//                logger.info("item1Count > totalCount / 2:{}", item1Count > totalCount / 2);
//                logger.info("item2Count > totalCount / 2:{}", item2Count > totalCount / 2);
//                logger.info("item3Count > totalCount / 2:{}", item3Count > totalCount / 2);
//                logger.info("item4Count > totalCount / 2:{}", item4Count > totalCount / 2);
                if (item1Count > totalCount / 2){
                    //如果过半的话审批通过
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项1");
                    this.updateById(voteEntity);
                }else if (item2Count > totalCount / 2){
                    //如果过半的话审批通过
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项2");
                    this.updateById(voteEntity);
                }else if (item2Count > totalCount / 2){
                    //如果过半的话审批通过
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项3");
                    this.updateById(voteEntity);
                }else if (item2Count > totalCount / 2){
                    //如果过半的话审批通过
                    voteEntity.setStatus(VoteStatusEnum.通过.getCode());
                    voteEntity.setVoteItem("选项4");
                    this.updateById(voteEntity);
                }else{
                    voteMemberQueryParam.setItemChoice(null);

                    //如果都选了而且没有一项是过半的
                    int votedCount = item1Count + item2Count + item3Count + item4Count + abandonCount;
                    if (votedCount == totalCount){
                        voteEntity.setStatus(VoteStatusEnum.不通过.getCode());
                        this.updateById(voteEntity);
                    }
                }
            }
        }
//        Long vid = 232L;
        Long vid = voteEntity.getId();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (beforeStatus == VoteStatusEnum.进行中.getCode() && voteEntity.getStatus() != VoteStatusEnum.进行中.getCode()) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", voteEntity.getUid()));
            if (mpUserEntity == null) return;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject voteRemind = new JSONObject();
            voteRemind.put("touser", openId);
            voteRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/votedetail/index?id=%s", vid));
            voteRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您发起的事项表决结果已产生");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "事项表决");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", VoteStatusEnum.getName(voteEntity.getStatus()));
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(new Date()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            voteRemind.put("data", data);
            System.out.println(voteRemind.toJSONString());
            mpUserService.pushTemplateMessage(voteRemind, voteEntity.getUid(), "vote");
            VoteMemberQueryParam voteMemberQueryParam2 = new VoteMemberQueryParam();
            //我觉得这里有问题，应该是这个
//            voteMemberQueryParam2.setVid(voteEntity.getId());
//            voteMemberQueryParam2.setVoteMemberType(VoteMemberTypeEnum.表决.getCode());
            voteMemberQueryParam.setVid(voteEntity.getId());
            voteMemberQueryParam.setVoteMemberType(VoteMemberTypeEnum.表决.getCode());

            List<VoteMemberEntity> voteMemberEntityList = voteMemberService.simpleList(voteMemberQueryParam2);
            for (VoteMemberEntity voteMemberEntity: voteMemberEntityList) {
                if (voteMemberEntity.getUid().equals(voteEntity.getUid()) ) {
                    continue;
                }
                MpUserEntity mpUserEntityMember = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", voteMemberEntity.getUid()));
                if (mpUserEntityMember == null) return;
                openId = mpUserEntity.getOpenidPublic();
                voteRemind = new JSONObject();
                voteRemind.put("touser", openId);
                voteRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
                miniProgram = new JSONObject();
                miniProgram.put("appid", "wx5e7524c02dade60a");
                miniProgram.put("pagepath", String.format("pages/votedetail/index?id=%s", vid));
                voteRemind.put("miniprogram", miniProgram);
                data = new JSONObject();
                first = new JSONObject();
                first.put("value", "您参与的事项表决结果已产生");
                keyword1 = new JSONObject();
                keyword1.put("value", "事项表决");
                keyword2 = new JSONObject();
                keyword2.put("value", VoteStatusEnum.getName(voteEntity.getStatus()));
                keyword3 = new JSONObject();
                keyword3.put("value", df.format(new Date()));
                data.put("first", first);
                data.put("keyword1", keyword1);
                data.put("keyword2", keyword2);
                data.put("keyword3", keyword3);
                voteRemind.put("data", data);
                System.out.println(voteRemind.toJSONString());
                mpUserService.pushTemplateMessage(voteRemind, voteEntity.getUid(), "vote");
            }
        }
    }

    /**
     * 保存更新多余信息
     * @param vote
     */
    private void saveOrUpdateExtra(VoteEntity vote) {
        //删除关联的文件
        multimediaService.delete(new EntityWrapper<MultimediaEntity>()
                .eq("related_id", vote.getId())
                .eq("related_type", BizTypeEnum.VOTE.getCode())
                .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        //并插入关联的文件
        List<FileEntity> fileEntityList =  vote.getFileList();
        if (null != fileEntityList && fileEntityList.size()>0){
            for (FileEntity fileEntity : fileEntityList) {
                System.out.println(fileEntity.getName());
                System.out.println(fileEntity.getRaw());
//                urlList.add(fileEntity.getResponse().getUrl());
                //保存图片
                MultimediaEntity multimediaEntity = new MultimediaEntity();
                multimediaEntity.setName(fileEntity.getName());
                multimediaEntity.setRelatedType(BizTypeEnum.VOTE.getCode());
                multimediaEntity.setRelatedId(vote.getId());
                multimediaEntity.setFileType(FileTypeEnum.PICTURE.getCode());
                if(null != fileEntity.getResponse()){
                    multimediaEntity.setUrl(fileEntity.getResponse().getUrl());
                }else{
                    multimediaEntity.setUrl(fileEntity.getUrl());
                }
                multimediaEntity.setName(multimediaEntity.getName());
                multimediaService.insert(multimediaEntity);
            }
        }
    }

    /**
     * 封装
     * @param vote
     * @param isInfo
     * @param uid
     */
    private void packageExtra(VoteEntity vote, boolean isInfo, Long uid){
        // 小区名称
        if (vote.getZoneId() != null) {
            ZonesEntity zonesEntity = zonesService.info(vote.getZoneId());
            if (zonesEntity != null) {
                vote.setZoneName(zonesEntity.getName());
            }
        }

        List<FileEntity> fileEntityList = new ArrayList<>();
        List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
                new EntityWrapper<MultimediaEntity>()
                        .eq("related_id", vote.getId())
                        .eq("related_type", BizTypeEnum.VOTE.getCode())
                        .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(multimediaEntity.getName());
            fileEntity.setUrl(multimediaEntity.getUrl());
            fileEntityList.add(fileEntity);
        }
        vote.setFileList(fileEntityList);
        VoteMemberQueryParam voteMemberQueryParam = new VoteMemberQueryParam();
        voteMemberQueryParam.setVid(vote.getId());
        voteMemberQueryParam.setVoteMemberType(VoteMemberTypeEnum.表决.getCode());
        //投票人员列表
        List<VoteMemberEntity> voteMemberEntityList = voteMemberService.simpleList(voteMemberQueryParam);
        for (VoteMemberEntity voteMemberEntity :voteMemberEntityList) {
            voteMemberEntity.setStatusCn(VoteMemberStatusEnum.getName(voteMemberEntity.getStatus()));
        }
        vote.setVoteMemberEntityList(voteMemberEntityList);
        //抄送人员列表
        voteMemberQueryParam.setVoteMemberType(VoteMemberTypeEnum.抄送.getCode());
        List<VoteMemberEntity> copy2VoteMemberEntityList = voteMemberService.simpleList(voteMemberQueryParam);
        vote.setCopy2VoteMemberEntityList(copy2VoteMemberEntityList);

        //投票标题 title
        //表决时间 createTime
        //表决方式 投票类型 1实名 2匿名 typeCn
        vote.setTypeCn(VoteTypeEnum.getName(vote.getType()));
        //表决规则 过半数则通过，超过表决截止时间算弃权。
        //选项列表 item1 - item4
        //投票详细 选项 几人 姓名列表
        if (vote.getItemType().equals(VoteItemTypeEnum.多选.getCode())){
            int item1Count = 0;
            int item2Count = 0;
            int item3Count = 0;
            int item4Count = 0;
            List<VoteMemberEntity> voteMemberItem1EntityList = new ArrayList<>();
            List<VoteMemberEntity> voteMemberItem2EntityList = new ArrayList<>();
            List<VoteMemberEntity> voteMemberItem3EntityList = new ArrayList<>();
            List<VoteMemberEntity> voteMemberItem4EntityList = new ArrayList<>();
            for (VoteMemberEntity voteMemberEntity : voteMemberEntityList) {
                if (voteMemberEntity.getItem1() == 1){
                    item1Count+=1;
                    voteMemberItem1EntityList.add(voteMemberEntity);
                }else if (voteMemberEntity.getItem2() == 1){
                    item2Count+=1;
                    voteMemberItem2EntityList.add(voteMemberEntity);
                }else if (voteMemberEntity.getItem3() == 1){
                    item3Count+=1;
                    voteMemberItem3EntityList.add(voteMemberEntity);
                }else if (voteMemberEntity.getItem4() == 1){
                    item4Count+=1;
                    voteMemberItem4EntityList.add(voteMemberEntity);
                }
            }
            vote.setItem1Count(item1Count);
            vote.setItem2Count(item2Count);
            vote.setItem3Count(item3Count);
            vote.setItem4Count(item4Count);
            vote.setVoteMemberItem1EntityList(voteMemberItem1EntityList);
            vote.setVoteMemberItem2EntityList(voteMemberItem2EntityList);
            vote.setVoteMemberItem3EntityList(voteMemberItem3EntityList);
            vote.setVoteMemberItem4EntityList(voteMemberItem4EntityList);
        }

        //状态 结果已出 选项3  voteItem
        //内容 content
        //图片 fileList
        //打印日期 printDate
        vote.setPrintDate(new Date());


        //投票标题 title
        //表决时间 createTime
        //表决方式 投票类型 1实名 2匿名 typeCn
        vote.setTypeCn(VoteTypeEnum.getName(vote.getType()));
        //表决规则 过半数则通过，超过表决截止时间算弃权。
        //投票详细 同意 反对 弃权 超时
        if (vote.getItemType().equals(VoteItemTypeEnum.单项.getCode())){
            int item1Count = 0;
            int item2Count = 0;
            int item3Count = 0;
            int item4Count = 0;
            List<VoteMemberEntity> voteMemberItem1EntityList = new ArrayList<>();
            List<VoteMemberEntity> voteMemberItem2EntityList = new ArrayList<>();
            List<VoteMemberEntity> voteMemberItem3EntityList = new ArrayList<>();
            List<VoteMemberEntity> voteMemberItem4EntityList = new ArrayList<>();
            for (VoteMemberEntity voteMemberEntity : voteMemberEntityList) {
                if (voteMemberEntity.getStatus() == VoteMemberStatusEnum.同意.getCode()){
                    item1Count+=1;
                    voteMemberItem1EntityList.add(voteMemberEntity);
                }else if (voteMemberEntity.getStatus() == VoteMemberStatusEnum.反对.getCode()){
                    item2Count+=1;
                    voteMemberItem2EntityList.add(voteMemberEntity);
                }else if (voteMemberEntity.getStatus() == VoteMemberStatusEnum.弃权.getCode()){
                    item3Count+=1;
                    voteMemberItem3EntityList.add(voteMemberEntity);
                }else if (voteMemberEntity.getStatus() == VoteMemberStatusEnum.超时.getCode()){
                    item4Count+=1;
                    voteMemberItem4EntityList.add(voteMemberEntity);
                }
            }
            vote.setItem1Count(item1Count);
            vote.setItem2Count(item2Count);
            vote.setItem3Count(item3Count);
            vote.setItem4Count(item4Count);
            vote.setVoteMemberItem1EntityList(voteMemberItem1EntityList);
            vote.setVoteMemberItem2EntityList(voteMemberItem2EntityList);
            vote.setVoteMemberItem3EntityList(voteMemberItem3EntityList);
            vote.setVoteMemberItem4EntityList(voteMemberItem4EntityList);
        }
        //状态 结果已出 statusCn
        vote.setStatusCn(VoteStatusEnum.getName(vote.getStatus()));
        //内容 content
        //图片 fileList
        //打印日期 printDate

        UserEntity userEntity = userService.selectById(vote.getUid());
        if (null != userEntity){
            vote.setRealname(userEntity.getRealname());
            vote.setAvatarUrl(userEntity.getAvatarUrl());
        }

        if (null != uid){
            VoteMemberEntity voteMemberEntity = voteMemberService.getByVidUid(vote.getId(), uid, VoteMemberTypeEnum.表决.getCode());
            if (voteMemberEntity != null){
                vote.setMemberStatus(voteMemberEntity.getStatus());
                vote.setMemberStatusCn(VoteMemberStatusEnum.getName(voteMemberEntity.getStatus()));
            }
        }
    }

    public void pushMessage() {

    }
}
