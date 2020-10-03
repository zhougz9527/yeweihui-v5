package com.yeweihui.modules.operation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.common.dao.MultimediaDao;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.enums.announce.paper.AnnounceMemberStatusEnum;
import com.yeweihui.modules.enums.announce.paper.AnnounceStatusEnum;
import com.yeweihui.modules.operation.dao.AnnounceDao;
import com.yeweihui.modules.operation.entity.AnnounceEntity;
import com.yeweihui.modules.operation.entity.AnnounceMemberEntity;
import com.yeweihui.modules.operation.service.AnnounceMemberService;
import com.yeweihui.modules.operation.service.AnnounceService;
import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.entity.SysUserRoleEntity;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.user.entity.MpUserEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.MpUserService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import com.yeweihui.modules.vo.api.form.announce.AnnounceOpeForm;
import com.yeweihui.modules.vo.query.AnnounceMemberQueryParam;
import com.yeweihui.modules.vo.query.AnnounceQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("announceService")
public class AnnounceServiceImpl extends ServiceImpl<AnnounceDao, AnnounceEntity> implements AnnounceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    MultimediaService multimediaService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    private AnnounceMemberService announceMemberService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private MpUserService mpUserService;

    @Resource
    MultimediaDao multimediaDao;

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
        List<AnnounceEntity> list = this.baseMapper.selectPageEn(page, params);
        for (AnnounceEntity announceEntity :list) {
            this.packageExtra(announceEntity, viewUid);
        }
        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    public AnnounceEntity info(Long id, UserEntity userEntity) {
        AnnounceEntity announce = this.selectById(id);
        this.packageExtra(announce, userEntity == null ? null : userEntity.getId());
        return announce;
    }

    @Override
    @Transactional
    public void save(AnnounceEntity announceEntity) {
        UserEntity userEntity = userService.selectById(announceEntity.getUid());
        if (null != userEntity) {
            announceEntity.setZoneId(userEntity.getZoneId());
        }


        List<AnnounceMemberEntity> announceMemberEntityList = announceEntity.getMemberEntityList();
        if (announceMemberEntityList == null || announceMemberEntityList.size() == 0){
            throw new RRException("请添加接收用户");
        }
        this.insert(announceEntity);


        for (AnnounceMemberEntity announceMemberEntity : announceMemberEntityList) {
            announceMemberEntity.setAid(announceEntity.getId());
            announceMemberEntity.setReferUid(announceEntity.getUid());
        }
        announceMemberService.insertBatch(announceMemberEntityList);

        Long aid = announceEntity.getId();
        UserEntity refUser = userService.selectById(announceEntity.getUid());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (AnnounceMemberEntity announceMemberEntity: announceMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", announceMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject announceRemind = new JSONObject();
            announceRemind.put("touser", openId);
            announceRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/formuladetail/index?id=%s", aid));
            announceRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一项新的公示记录");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "公示记录");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", refUser.getRealname());
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(announceEntity.getCreateTime()));
            JSONObject keyword4 = new JSONObject();
            keyword4.put("value", refUser.getRoleName());
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("keyword4", keyword4);
            announceRemind.put("data", data);
            mpUserService.pushTemplateMessage(announceRemind, announceMemberEntity.getUid(), "announce");
        }


        this.saveOrUpdateExtra(announceEntity);
    }

    @Override
    @Transactional
    public void update(AnnounceEntity announce) {
        this.updateAllColumnById(announce);//全部更新
        this.saveOrUpdateExtra(announce);
    }

    /**
     * 保存更新多余信息
     * @param announceEntity
     */
    private void saveOrUpdateExtra(AnnounceEntity announceEntity) {
        Wrapper<MultimediaEntity> eq = new EntityWrapper<MultimediaEntity>()
                .eq("related_id", announceEntity.getId())
                .eq("related_type", BizTypeEnum.Announcement.getCode())
                .eq("file_type", FileTypeEnum.PICTURE.getCode());
        List<MultimediaEntity> multiList = multimediaDao.selectList(eq);
        Date createTime = null;
        if (null != multiList && multiList.size() > 0) {
            createTime = multiList.get(0).getCreateTime();
        }

        //删除关联的文件
        multimediaService.delete(eq);
        //并插入关联的文件
        List<FileEntity> fileEntityList =  announceEntity.getFileList();
        if (null != fileEntityList && fileEntityList.size()>0){
            for (FileEntity fileEntity : fileEntityList) {
//                urlList.add(fileEntity.getResponse().getUrl());
                //保存图片
                MultimediaEntity multimediaEntity = new MultimediaEntity();
//                if (null != fileEntity.getCreateTime() && null == createTime) {
//                    multimediaEntity.setCreateTime(fileEntity.getCreateTime());
//                } else {
//                    multimediaEntity.setCreateTime(createTime);
//                }
//                if (null != createTime) {
//                    multimediaEntity.setCreateTime(createTime);
//                }
                multimediaEntity.setName(fileEntity.getName());
                multimediaEntity.setRelatedType(BizTypeEnum.Announcement.getCode());
                multimediaEntity.setRelatedId(announceEntity.getId());
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

        // 重新赋值创建时间避免被覆盖
        MultimediaEntity multimediaEntity = new MultimediaEntity();
        multimediaEntity.setCreateTime(createTime);
        multimediaDao.update(multimediaEntity, eq);

    }

    /**
     * 封装
     * @param announceEntity
     * @param uid
     */
    private void packageExtra(AnnounceEntity announceEntity, Long uid){
        List<FileEntity> fileEntityList = new ArrayList<>();
        List<FileEntity> publicityFileEntityList = new ArrayList<>();
        String fileTypeCode = FileTypeEnum.PICTURE.getCode();// 非公示完成时上传的文件
        String publicityFileTypeCode = FileTypeEnum.PUBLICITY.getCode();// 公示完成时上传的文件
//        List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
//                new EntityWrapper<MultimediaEntity>()
//                        .eq("related_id", announceEntity.getId())
//                        .eq("related_type", BizTypeEnum.Announcement.getCode())
//                        .eq("file_type", fileTypeCode)
//                        .or()
//                        .eq("file_type", publicityFileTypeCode)
//        );
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("aid", announceEntity.getId());
        paramsMap.put("relatedType", BizTypeEnum.Announcement.getCode());
        paramsMap.put("fileType", fileTypeCode);
        paramsMap.put("publicityFileType", publicityFileTypeCode);
        List<MultimediaEntity> multimediaEntityList = multimediaDao.findList(paramsMap);
        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(multimediaEntity.getName());
            fileEntity.setUrl(multimediaEntity.getUrl());
            // 文件的上传时间
            fileEntity.setCreateTime(multimediaEntity.getCreateTime());
            if (publicityFileTypeCode.equals(multimediaEntity.getFileType())) {// 公示完成时上传的文件
                publicityFileEntityList.add(fileEntity);
            } else {// 非公示完成时上传的文件
                fileEntityList.add(fileEntity);
            }
        }
        announceEntity.setFileList(fileEntityList);
        announceEntity.setPublicityFileList(publicityFileEntityList);

        UserEntity userEntity = userService.selectById(announceEntity.getUid());
        if (null != userEntity){
            announceEntity.setRealname(userEntity.getRealname());
            announceEntity.setAvatarUrl(userEntity.getAvatarUrl());
        }

        AnnounceMemberQueryParam announceMemberQueryParam = new AnnounceMemberQueryParam();
        announceMemberQueryParam.setAid(announceEntity.getId());
        List<AnnounceMemberEntity> announceMemberEntityList = announceMemberService.simpleList(announceMemberQueryParam);
        for (AnnounceMemberEntity announceMemberEntity : announceMemberEntityList) {
            announceMemberEntity.setStatusCn(AnnounceMemberStatusEnum.getName(announceMemberEntity.getStatus()));
        }
        announceEntity.setMemberEntityList(announceMemberEntityList);

        announceEntity.setStatusCn(AnnounceStatusEnum.getName(announceEntity.getStatus()));

        if (uid != null) {
            announceMemberQueryParam.setViewUid(uid);
            AnnounceMemberEntity announceMemberEntity = announceMemberService.selectOne(new EntityWrapper<AnnounceMemberEntity>()
                    .eq("aid", announceEntity.getId())
                    .eq("uid", uid));
            if(announceMemberEntity != null){
                announceEntity.setMemberStatus(announceMemberEntity.getStatus());
                announceEntity.setMemberStatusCn(AnnounceMemberStatusEnum.getName(announceMemberEntity.getStatus()));
            }
        }

        if (announceEntity.getZoneId()!=null){
            ZonesEntity zonesEntity = zonesService.selectById(announceEntity.getZoneId());
            if (null != zonesEntity){
                announceEntity.setZoneName(zonesEntity.getName());
            }
        }
    }

    /**
     * 公告结束
     * @param announceOpeForm
     */
    @Override
    public void finish(AnnounceOpeForm announceOpeForm) {
        AnnounceEntity announceEntity = this.info(announceOpeForm.getAid(), null);
        List<Long> memberUidList = new ArrayList<>();
        memberUidList.add(announceOpeForm.getUid());
        List<AnnounceMemberEntity> announceMemberList = announceMemberService.selectList(new EntityWrapper<AnnounceMemberEntity>().eq("aid", announceEntity.getId()).in("uid", memberUidList));
        if (null == announceMemberList || announceMemberList.size() == 0) {
            throw new RRException("非收件人无法完成公示!");
        }
        Date now = new Date();
        if (!now.after(announceEntity.getEndTime())) {
            throw new RRException("还未结束公示!");
        }
        if (announceEntity.getStatus().equals(AnnounceStatusEnum.完成.getCode())) {
            throw new RRException("公示已完成");
        }
        if (announceOpeForm != null && "finish".equalsIgnoreCase(announceOpeForm.getOpeName())) {
            String urls = announceOpeForm.getUrls();// 附件的url 用逗号分隔
            if (StringUtils.isNotEmpty(urls)) {
                Long announceId = announceOpeForm.getAid();
                String[] urlArray = urls.split(",");
                for (int i = 0; i < urlArray.length; i++) {
                    String suffix = urlArray[i].substring(urlArray[i].lastIndexOf("."));
                    MultimediaEntity multimediaEntity = new MultimediaEntity(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                            System.currentTimeMillis() + suffix, urlArray[i], 0, null, announceId, 9, FileTypeEnum.PUBLICITY.getName());
                    multimediaDao.insertAndGetId(multimediaEntity);
                }
            }
//            AnnounceEntity announceEntity = this.info(announceOpeForm.getAid(), null);
//            List<Long> memberUidList = new ArrayList<>();
//            memberUidList.add(announceOpeForm.getUid());
//            List<AnnounceMemberEntity> announceMemberList = announceMemberService.selectList(new EntityWrapper<AnnounceMemberEntity>().eq("aid", announceEntity.getId()).in("uid", memberUidList));
//            if (null != announceMemberList && announceMemberList.size() > 0) {
//                Date now = new Date();
//                if (now.after(announceEntity.getEndTime())) {
                    announceEntity.setStatus(AnnounceStatusEnum.完成.getCode());
                    announceEntity.setFinishUrl(announceOpeForm.getFinishUrl());
                    announceEntity.setRemark(announceOpeForm.getRemark());
                    this.update(announceEntity);
                    Long aid = announceEntity.getId();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", announceEntity.getUid()));
                    if (mpUserEntity == null) return;
                    String openId = mpUserEntity.getOpenidPublic();
                    JSONObject announceRemind = new JSONObject();
                    announceRemind.put("touser", openId);
                    announceRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
                    JSONObject miniProgram = new JSONObject();
                    miniProgram.put("appid", "wx5e7524c02dade60a");
                    miniProgram.put("pagepath", String.format("pages/formuladetail/index?id=%s", aid));
                    announceRemind.put("miniprogram", miniProgram);
                    JSONObject data = new JSONObject();
                    JSONObject first = new JSONObject();
                    first.put("value", "您有一项公示记录已完成");
                    JSONObject keyword1 = new JSONObject();
                    keyword1.put("value", "公示记录");
                    JSONObject keyword2 = new JSONObject();
                    keyword2.put("value", AnnounceStatusEnum.getName(announceEntity.getStatus()));
                    JSONObject keyword3 = new JSONObject();
                    keyword3.put("value", df.format(new Date()));
                    data.put("first", first);
                    data.put("keyword1", keyword1);
                    data.put("keyword2", keyword2);
                    data.put("keyword3", keyword3);
                    announceRemind.put("data", data);
                    System.out.println(announceRemind.toJSONString());
                    mpUserService.pushTemplateMessage(announceRemind, announceEntity.getUid(), "announce");
                    AnnounceMemberQueryParam announceMemberQueryParam = new AnnounceMemberQueryParam();
                    announceMemberQueryParam.setAid(aid);
                    List<AnnounceMemberEntity> announceMemberEntityList = announceMemberService.simpleList(announceMemberQueryParam);
                    for (AnnounceMemberEntity announceMemberEntity: announceMemberEntityList) {
                        if (announceMemberEntity.getUid().equals(announceEntity.getUid())) {
                            continue;
                        }
                        mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", announceEntity.getUid()));
                        if (mpUserEntity == null) return;
                        openId = mpUserEntity.getOpenidPublic();
                        announceRemind = new JSONObject();
                        announceRemind.put("touser", openId);
                        announceRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
                        miniProgram = new JSONObject();
                        miniProgram.put("appid", "wx5e7524c02dade60a");
                        miniProgram.put("pagepath", String.format("pages/formuladetail/index?id=%s", aid));
                        announceRemind.put("miniprogram", miniProgram);
                        data = new JSONObject();
                        first = new JSONObject();
                        first.put("value", "您有一项公示记录已完成");
                        keyword1 = new JSONObject();
                        keyword1.put("value", "公示记录");
                        keyword2 = new JSONObject();
                        keyword2.put("value", AnnounceStatusEnum.getName(announceEntity.getStatus()));
                        keyword3 = new JSONObject();
                        keyword3.put("value", df.format(new Date()));
                        data.put("first", first);
                        data.put("keyword1", keyword1);
                        data.put("keyword2", keyword2);
                        data.put("keyword3", keyword3);
                        announceRemind.put("data", data);
                        mpUserService.pushTemplateMessage(announceRemind, announceEntity.getUid(), "announce");
                    }
//                } else {
//                    throw new RRException("还未结束公示!");
//                }
//            } else {
//                throw new RRException("非收件人无法完成公示!");
//            }
        } else {
            throw new RRException("非法操作");
        }

    }

    @Override
    public void expireAnnounce() {
        AnnounceQueryParam announceQueryParam = new AnnounceQueryParam();
        announceQueryParam.setExpire("true");
        announceQueryParam.setStatus(0);
        List<AnnounceEntity> announceEntityList = this.baseMapper.simpleList(announceQueryParam);
        System.out.println(announceEntityList);
        for (AnnounceEntity announceEntity: announceEntityList) {
            AnnounceOpeForm announceOpeForm = new AnnounceOpeForm();
            announceOpeForm.setAid(announceEntity.getId());
            announceOpeForm.setOpeName("finish");
            announceOpeForm.setUid(announceEntity.getUid());
            finish(announceOpeForm);
//            announceEntity.setStatus(AnnounceStatusEnum.完成.getCode());
        }
//        this.updateBatchById(announceEntityList);

    }

    @Override
    public List<AnnounceEntity> simpleList(AnnounceQueryParam announceQueryParam) {
        return this.baseMapper.simpleList(announceQueryParam);
    }

}
