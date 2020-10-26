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
import com.yeweihui.modules.common.service.impl.MultimediaServiceImpl;
import com.yeweihui.modules.operation.dao.NoticeDao;
import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.operation.entity.NoticeMemberEntity;
import com.yeweihui.modules.operation.entity.NoticeReadRecordEntity;
import com.yeweihui.modules.operation.service.NoticeMemberService;
import com.yeweihui.modules.operation.service.NoticeReadRecordService;
import com.yeweihui.modules.operation.service.NoticeService;
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
import com.yeweihui.modules.vo.query.NoticeMemberQueryParam;
import com.yeweihui.modules.vo.query.NoticeQueryParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeDao, NoticeEntity> implements NoticeService {

    private static final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    ZonesService zonesService;

    @Autowired
    NoticeMemberService noticeMemberService;

    @Autowired
    private NoticeReadRecordService noticeReadRecordService;

    @Autowired
    MultimediaServiceImpl multimediaService;

    @Autowired
    MpUserService mpUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    private ExecutorService asyncPool = new ThreadPoolExecutor(10, 100, 5L, TimeUnit.MINUTES,
            new LinkedBlockingDeque<Runnable>(10000));

    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        Long viewUid = (Long) params.get("viewUid");
//        if (viewUid == null) {
//            viewUid = ShiroUtils.getUserId();
//        }
        int minRecordStatus = 2;
        if (null != viewUid && viewUid == Constant.SUPER_ADMIN) {
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
        List<NoticeEntity> list = this.baseMapper.selectPageEn(page, params);
        System.out.println(page.getTotal());
        for (NoticeEntity notice:list) {
            this.packageExtra(notice, viewUid);
        }

        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public NoticeEntity info(Long id, UserEntity userEntity) {
        NoticeEntity notice = this.selectById(id);
        if (userEntity != null && userEntity.getId() != null) {
            this.packageExtra(notice, userEntity.getId());
            NoticeMemberEntity noticeMemberEntity = noticeMemberService.selectOne(new EntityWrapper<NoticeMemberEntity>()
                    .eq("nid", notice.getId())
                    .eq("uid", userEntity.getId()));
            if (noticeMemberEntity != null) {
                noticeMemberEntity.setStatus(2);
                noticeMemberEntity.setLastReadTime(new Date());
                noticeMemberService.updateById(noticeMemberEntity);
            }
            // 写入已读表
            NoticeReadRecordEntity noticeReadRecordEntity = noticeReadRecordService.selectOne(new EntityWrapper<NoticeReadRecordEntity>()
                            .eq("nid", notice.getId())
                            .eq("uid", userEntity.getId()));
            if (noticeReadRecordEntity == null) {
                noticeReadRecordEntity = new NoticeReadRecordEntity();
                noticeReadRecordEntity.setNid(notice.getId());
                noticeReadRecordEntity.setUid(userEntity.getId());
            }
            noticeReadRecordEntity.setReadTime(new Date());
            noticeReadRecordService.insertOrUpdate(noticeReadRecordEntity);
        }

        asyncPool.execute(() -> {
            try {
                baseMapper.updateReadCount(notice);
            } catch (Exception e) {
                logger.error("update read count error, param={}", notice, e);
            }
        });
        return notice;
    }

    @Override
    public List<NoticeReadRecordEntity> getReadMembers(Long nid) {
        return noticeReadRecordService.getReadMembers(nid);
    }

    @Override
    public int getCount(NoticeQueryParam noticeQueryParam) {
        return this.baseMapper.getCount(noticeQueryParam);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(NoticeEntity notice) {
        UserEntity userEntity = userService.selectById(notice.getUid());
        if (null != userEntity) {
            notice.setZoneId(userEntity.getZoneId());
        }
        // 处理通知小区
        Set<Long> noticeIdSet = notice.getZoneIdSet();
        if (CollectionUtils.isEmpty(noticeIdSet)) {
            throw new RRException("noticeIdSet没有传");
        }
        StringBuilder sb = new StringBuilder().append("#");
        noticeIdSet.forEach( id -> sb.append(id.toString()).append("#"));
        notice.setNoticeZoneIdTag(sb.toString());

        this.insert(notice);
        List<NoticeMemberEntity> noticeMemberEntityList = notice.getNoticeMemberEntityList();
        if (noticeMemberEntityList == null || noticeMemberEntityList.size() == 0) {
            throw new RRException("请确认通知人员");
        }
        for (NoticeMemberEntity noticeMemberEntity: noticeMemberEntityList) {
            noticeMemberEntity.setNid(notice.getId());
            noticeMemberEntity.setReferUid(notice.getUid());
            noticeMemberEntity.setStatus(1);
        }
        noticeMemberService.insertBatch(noticeMemberEntityList);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (NoticeMemberEntity noticeMemberEntity: noticeMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", noticeMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject noticeRemind = new JSONObject();
            noticeRemind.put("touser", openId);
            noticeRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/noticedetail/index?id=%s", notice.getId()));
            noticeRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有新的通知公告");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "通知公告");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", userEntity.getRealname());
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(notice.getCreateTime()));
            JSONObject keyword4 = new JSONObject();
            keyword4.put("value", sysRoleService.getHighestLevelRoleNameByUserId(userEntity.getId()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("keyword4", keyword4);
            noticeRemind.put("data", data);
            System.out.println(noticeRemind.toJSONString());
            mpUserService.pushTemplateMessage(noticeRemind, notice.getUid(), "notice");
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void update(NoticeEntity notice) {
        this.updateById(notice);
        noticeMemberService.delete(new EntityWrapper<NoticeMemberEntity>()
                .eq("nid", notice.getId()));

        List<NoticeMemberEntity> noticeMemberEntityList = notice.getNoticeMemberEntityList();
        if (noticeMemberEntityList == null || noticeMemberEntityList.size() == 0) {
            throw new RRException("请确认通知人员");
        }
        for (NoticeMemberEntity noticeMemberEntity: noticeMemberEntityList) {
            noticeMemberEntity.setNid(notice.getId());
            noticeMemberEntity.setReferUid(notice.getUid());
            noticeMemberEntity.setStatus(1);
        }
        noticeMemberService.insertBatch(noticeMemberEntityList);
    }

    private void packageExtra(NoticeEntity noticeEntity, Long uid) {
        UserEntity userEntity = userService.selectById(noticeEntity.getUid());
        if (null != userEntity){
            noticeEntity.setRealname(userEntity.getRealname());
            noticeEntity.setAvatarUrl(userEntity.getAvatarUrl());
            // set creator group
            noticeEntity.setCreatorGroup(userService.getGroupByRoleName(userEntity.getRoleName()));
        }
        if (noticeEntity.getZoneId()!=null){
            ZonesEntity zonesEntity = zonesService.selectById(noticeEntity.getZoneId());
            if (null != zonesEntity){
                noticeEntity.setZoneName(zonesEntity.getName());
            }
        }
        NoticeMemberQueryParam param = new NoticeMemberQueryParam();
        param.setNid(noticeEntity.getId());
        List<NoticeMemberEntity> noticeMemberEntityList = noticeMemberService.simpleList(param);
        noticeEntity.setNoticeMemberEntityList(noticeMemberEntityList);
        if (uid != null) {
            NoticeMemberEntity noticeMemberEntity = noticeMemberService.selectOne(new EntityWrapper<NoticeMemberEntity>()
                    .eq("nid", noticeEntity.getId())
                    .eq("uid", uid));
            if (noticeMemberEntity != null) {
                noticeEntity.setMemberStatus(noticeMemberEntity.getStatus());
            }
        }
        // 丰富一下小区名称
        String[] idArray = noticeEntity.getNoticeZoneIdTag().split("#");
        List<String> zoneNameList = new ArrayList<>();
        if (idArray.length > 0) {
            for (String id : idArray) {
                if (StringUtils.isNotEmpty(id)) {
                    ZonesEntity zone = zonesService.getById(Long.valueOf(id));
                    zoneNameList.add(zone.getName());
                }
            }
            noticeEntity.setZoneNameListStr(String.join(", ", zoneNameList));
        }
    }
}
