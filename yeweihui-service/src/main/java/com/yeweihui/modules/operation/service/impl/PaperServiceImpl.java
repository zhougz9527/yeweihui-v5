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
import com.yeweihui.common.validator.Assert;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.enums.paper.PaperMemberStatusEnum;
import com.yeweihui.modules.enums.paper.PaperStatusEnum;
import com.yeweihui.modules.operation.dao.PaperDao;
import com.yeweihui.modules.operation.entity.PaperMemberEntity;
import com.yeweihui.modules.operation.entity.PaperEntity;
import com.yeweihui.modules.operation.entity.PaperMemberEntity;
import com.yeweihui.modules.operation.service.PaperMemberService;
import com.yeweihui.modules.operation.service.PaperService;
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
import com.yeweihui.modules.vo.api.form.paper.PaperSignForm;
import com.yeweihui.modules.vo.query.PaperMemberQueryParam;
import com.yeweihui.modules.vo.query.PaperQueryParam;
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


@Service("paperService")
public class PaperServiceImpl extends ServiceImpl<PaperDao, PaperEntity> implements PaperService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    MultimediaService multimediaService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    PaperMemberService paperMemberService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    MpUserService mpUserService;

    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        /*Page<PaperEntity> page = this.selectPage(
                new Query<PaperEntity>(params).getPage(),
                new EntityWrapper<PaperEntity>()
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
        List<PaperEntity> list = this.baseMapper.selectPageEn(page, params);
        for (PaperEntity paperEntity :list) {
            this.packageExtra(paperEntity, false, viewUid);
        }
        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    public PaperEntity info(Long id, UserEntity userEntity) {
        PaperEntity paper = this.selectById(id);
        if (userEntity != null) {
            this.packageExtra(paper, true, userEntity.getId());
        } else {
            this.packageExtra(paper, true, null);
        }
        return paper;
    }

    @Override
    @Transactional
    public void save(PaperEntity paper) {
        UserEntity userEntity = userService.selectById(paper.getUid());
        if (null != userEntity){
            paper.setZoneId(userEntity.getZoneId());
        }

        /*//获取当前角色组的角色roleCodeList
        List<String> roleCodeList = sysRoleService.getRoleCodeByGroup("业主委员会");
        //根据小区id和角色列表获取用户人员列表
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setZoneId(paper.getZoneId());
        userQueryParam.setRoleCodeList(roleCodeList);
        List<UserEntity> userEntityList = userService.simpleList(userQueryParam);*/
        //需要审批的总人数是业主委员会的全体成员


        List<PaperMemberEntity> paperMemberEntityList = paper.getPaperMemberEntityList();
        if (paperMemberEntityList == null || paperMemberEntityList.size() == 0){
            throw new RRException("请添加收函用户");
        }
        paper.setTotal(paperMemberEntityList.size());
        this.insert(paper);

        /*for (int i = 0; i < userEntityList.size(); i++) {
            UserEntity userEntity1 = userEntityList.get(i);
            PaperMemberEntity paperMemberEntity = new PaperMemberEntity();
            paperMemberEntity.setPid(paper.getId());
            paperMemberEntity.setReferUid(paper.getUid());
            paperMemberEntity.setUid(userEntity1.getId());
            paperMemberService.insert(paperMemberEntity);
        }*/

        for (PaperMemberEntity paperMemberEntity : paperMemberEntityList) {
            paperMemberEntity.setPid(paper.getId());
            paperMemberEntity.setReferUid(paper.getUid());
            paperMemberService.insert(paperMemberEntity);
        }

        Long pid = paper.getId();
        UserEntity refUser = userService.selectById(paper.getUid());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (PaperMemberEntity paperMemberEntity: paperMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", paperMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject paperRemind = new JSONObject();
            paperRemind.put("touser", openId);
            paperRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/paperdetail/index?id=%s", pid));
            paperRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一项新的网上发函");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "网上发函");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", refUser.getRealname());
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(paper.getCreateTime()));
            JSONObject keyword4 = new JSONObject();
            keyword4.put("value", sysRoleService.getHighestLevelRoleNameByUserId(refUser.getId()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("keyword4", keyword4);
            paperRemind.put("data", data);
            System.out.println(paperRemind);
            mpUserService.pushTemplateMessage(paperRemind, paperMemberEntity.getUid(), "paper");
        }


        this.saveOrUpdateExtra(paper);
    }

    @Override
    @Transactional
    public void update(PaperEntity paper) {
        this.updateAllColumnById(paper);//全部更新
        this.saveOrUpdateExtra(paper);
    }

    @Override
    public int getCount(PaperQueryParam paperQueryParam) {
        return this.baseMapper.getCount(paperQueryParam);
    }

    /**
     * 签收发函
     * @param paperSignForm
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void paperSign(PaperSignForm paperSignForm) {
        PaperEntity paperEntity = this.selectById(paperSignForm.getPid());
        Assert.isNull(paperEntity, "函件不存在");
        if (paperEntity.getStatus().equals(PaperStatusEnum.已签收.getCode())){
            throw new RRException("当前函件状态"+ PaperStatusEnum.getName(paperEntity.getStatus()) +"不能签收");
        }
        PaperMemberEntity paperMemberEntity = paperMemberService.getByPidUid(paperSignForm.getPid(), paperSignForm.getUid());
        Assert.isNull(paperMemberEntity, "当前用户无对当前函件签收的权限");
        if(paperMemberEntity.getStatus().equals(PaperMemberStatusEnum.已签收.getCode())){
            throw new RRException("当前函件签收状态"+ PaperMemberStatusEnum.getName(paperMemberEntity.getStatus()) +"不可签收");
        }
        paperMemberEntity.setStatus(PaperMemberStatusEnum.已签收.getCode());
        paperMemberEntity.setSignTime(new Date());
        paperMemberEntity.setVerifyUrl(paperSignForm.getVerifyUrl());
        paperMemberService.updateById(paperMemberEntity);

        PaperMemberQueryParam paperMemberQueryParam = new PaperMemberQueryParam();
        paperMemberQueryParam.setPid(paperSignForm.getPid());
        paperMemberQueryParam.setPaperMemberStatus(PaperMemberStatusEnum.未签收.getCode());

//        //需要签收人数
//        Integer total;
//        //已经签收人数
//        Integer checked;

        //已签收人数+1
        paperEntity.setChecked(paperEntity.getChecked()+1);
        //未签收数量为0则函件状态变为签收
        int notSignCount = paperMemberService.getCount(paperMemberQueryParam);
        logger.info("paperSign notSignCount:{}", notSignCount);
        if (notSignCount == 0){
            paperEntity.setStatus(PaperStatusEnum.已签收.getCode());
        }
        this.updateById(paperEntity);

    }

    /**
     * 保存更新多余信息
     * @param paper
     */
    private void saveOrUpdateExtra(PaperEntity paper){
        //删除关联的文件
        multimediaService.delete(new EntityWrapper<MultimediaEntity>()
                .eq("related_id", paper.getId())
                .eq("related_type", BizTypeEnum.PAPER.getCode())
                .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        //并插入关联的文件
        List<FileEntity> fileEntityList =  paper.getFileList();
        if (null != fileEntityList && fileEntityList.size()>0){
            for (FileEntity fileEntity : fileEntityList) {
//                urlList.add(fileEntity.getResponse().getUrl());
                //保存图片
                MultimediaEntity multimediaEntity = new MultimediaEntity();
                multimediaEntity.setName(fileEntity.getName());
                multimediaEntity.setRelatedType(BizTypeEnum.PAPER.getCode());
                multimediaEntity.setRelatedId(paper.getId());
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
     * @param paper
     * @param isInfo
     * @param uid
     */
    private void packageExtra(PaperEntity paper, boolean isInfo, Long uid){
        List<FileEntity> fileEntityList = new ArrayList<>();
        List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
                new EntityWrapper<MultimediaEntity>()
                        .eq("related_id", paper.getId())
                        .eq("related_type", BizTypeEnum.PAPER.getCode())
                        .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(multimediaEntity.getName());
            fileEntity.setUrl(multimediaEntity.getUrl());
            fileEntityList.add(fileEntity);
        }
        paper.setFileList(fileEntityList);

        UserEntity userEntity = userService.selectById(paper.getUid());
        if (null != userEntity){
            paper.setRealname(userEntity.getRealname());
            paper.setAvatarUrl(userEntity.getAvatarUrl());
        }

        List<PaperMemberEntity> paperMemberEntityList = paperMemberService.getListByPid(paper.getId());
        for (PaperMemberEntity paperMemberEntity : paperMemberEntityList) {
            paperMemberEntity.setStatusCn(PaperMemberStatusEnum.getName(paperMemberEntity.getStatus()));
        }
        paper.setPaperMemberEntityList(paperMemberEntityList);

        paper.setStatusCn(PaperStatusEnum.getName(paper.getStatus()));

        if (uid != null){
            PaperMemberEntity paperMemberEntity = paperMemberService.getByPidUid(paper.getId(), uid);
            if(paperMemberEntity != null){
                paper.setMemberStatus(paperMemberEntity.getStatus());
                paper.setMemberStatusCn(PaperMemberStatusEnum.getName(paperMemberEntity.getStatus()));
            }
        }

        if (paper.getZoneId()!=null){
            ZonesEntity zonesEntity = zonesService.selectById(paper.getZoneId());
            if (null != zonesEntity){
                paper.setZoneName(zonesEntity.getName());
            }
        }

    }

}
