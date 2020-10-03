package com.yeweihui.modules.operation.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.*;
import com.yeweihui.common.validator.Assert;
import com.yeweihui.modules.common.entity.MultimediaEntity;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.enums.request.*;
import com.yeweihui.modules.operation.dao.RequestDao;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.operation.entity.RequestMemberEntity;
import com.yeweihui.modules.operation.service.RequestMemberService;
import com.yeweihui.modules.operation.service.RequestService;
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
import com.yeweihui.modules.vo.api.form.request.RequestCancelForm;
import com.yeweihui.modules.vo.api.form.request.RequestVerifyForm;
import com.yeweihui.modules.vo.query.RequestQueryParam;
import com.yeweihui.modules.vo.query.UserQueryParam;
import com.yeweihui.modules.vo.query.RequestMemberQueryParam;
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


@Service("requestService")
public class RequestServiceImpl extends ServiceImpl<RequestDao, RequestEntity> implements RequestService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    MultimediaService multimediaService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    RequestMemberService requestMemberService;

    @Autowired
    ZonesService zonesService;

    @Autowired
    MpUserService mpUserService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        /*Page<RequestEntity> page = this.selectPage(
                new Query<RequestEntity>(params).getPage(),
                new EntityWrapper<RequestEntity>()
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
        params.forEach((k, v) -> logger.info("k --> " + k + ", v --> " + v));
        List<RequestEntity> list = this.baseMapper.selectPageEn(page, params);
        for (RequestEntity requestEntity : list) {
            logger.info("request --> " + requestEntity.toString());
            this.packageExtra(requestEntity, false, viewUid);
        }
        page.setRecords(list);

        return new PageUtils(page);
    }

    /**
     * 保存
     * @param request
     */
    @Override
    @Transactional
    public void save(RequestEntity request) {
        UserEntity userEntity = userService.selectById(request.getUid());
        if (null != userEntity){
            request.setUname(userEntity.getRealname());
            request.setZoneId(userEntity.getZoneId());
        }

        int stepTotal = 0;
        List<UserEntity> userEntityList = null;
        List<RequestMemberEntity> verifyMemberEntityList = request.getVerifyMemberEntityList();
        //如果审核人员有传的话则使用传来的，不然则使用后台获取的所有业主委员会的用户
        if (null != verifyMemberEntityList && verifyMemberEntityList.size()>0){
            stepTotal = verifyMemberEntityList.size();
        }else{
            //获取当前角色组的角色roleCodeList
            List<String> roleCodeList = sysRoleService.getRoleCodeByGroup("业主委员会");
            //根据小区id和角色列表获取用户人员列表
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setZoneId(request.getZoneId());
            userQueryParam.setRoleCodeList(roleCodeList);
            userEntityList = userService.simpleList(userQueryParam);

            stepTotal = userEntityList.size();
        }

        //需要审批的总人数是业主委员会的全体成员
        request.setStepTotal(stepTotal);
        request.setStatus(RequestStatusEnum.审核中.getCode());

        this.insert(request);

        if (null != verifyMemberEntityList && verifyMemberEntityList.size()>0){
            for (int i = 0; i < verifyMemberEntityList.size(); i++) {
                RequestMemberEntity requestMemberEntity = verifyMemberEntityList.get(i);
                requestMemberEntity.setRid(request.getId());
//            requestMemberEntity.setUid(userEntity1.getId());
                requestMemberEntity.setReferUid(request.getUid());
                requestMemberEntity.setType(RequestOpeTypeEnum.审批.getCode());//未涉及到抄送的
                requestMemberEntity.setStep(i+1);
                requestMemberEntity.setStatus(RequestMemberVerifyStatusEnum.未审核.getCode());
                requestMemberService.insert(requestMemberEntity);
            }
        }else{
            for (int i = 0; i < userEntityList.size(); i++) {
                UserEntity userEntity1 = userEntityList.get(i);
                RequestMemberEntity requestMemberEntity = new RequestMemberEntity();
                requestMemberEntity.setRid(request.getId());
                requestMemberEntity.setUid(userEntity1.getId());
                requestMemberEntity.setReferUid(request.getUid());
                requestMemberEntity.setType(RequestOpeTypeEnum.审批.getCode());//未涉及到抄送的
                requestMemberEntity.setStep(i+1);
                requestMemberEntity.setStatus(RequestMemberVerifyStatusEnum.未审核.getCode());
                requestMemberService.insert(requestMemberEntity);
                verifyMemberEntityList.add(requestMemberEntity);
            }
        }

        List<RequestMemberEntity> copyToMemberEntityList = request.getCopyToMemberEntityList();
        if (null != copyToMemberEntityList && copyToMemberEntityList.size()>0){
            for (int i = 0; i < copyToMemberEntityList.size(); i++) {
                RequestMemberEntity requestMemberEntity = copyToMemberEntityList.get(i);
                requestMemberEntity.setRid(request.getId());
//            requestMemberEntity.setUid(userEntity1.getId());
                requestMemberEntity.setReferUid(request.getUid());
                requestMemberEntity.setType(RequestOpeTypeEnum.抄送.getCode());//未涉及到抄送的
                requestMemberEntity.setStep(i+1);
                requestMemberEntity.setStatus(RequestMemberVerifyStatusEnum.未审核.getCode());
                requestMemberService.insert(requestMemberEntity);
            }
        }

        this.saveOrUpdateExtra(request);

        Long rid = request.getId();
//        Long vid = 232L; //测试

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (RequestMemberEntity requestMemberEntity: verifyMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", requestMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject requestRemind = new JSONObject();
            requestRemind.put("touser", openId);
            requestRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/stampdetail/index?id=%s", rid));
            requestRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一项新的用章申请审批");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "用章申请");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", userEntity.getRealname());
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(request.getCreateTime()));
            JSONObject keyword4 = new JSONObject();
            keyword4.put("value", userEntity.getRoleName());
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("keyword4", keyword4);
            requestRemind.put("data", data);
            mpUserService.pushTemplateMessage(requestRemind, requestMemberEntity.getUid(), "request");
        }

        // 推送消息给抄送人
        if (copyToMemberEntityList == null) {
            return;
        }
        for (RequestMemberEntity requestMemberEntity: copyToMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", requestMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject requestCopyRemind = new JSONObject();
            requestCopyRemind.put("touser", openId);
            requestCopyRemind.put("template_id", "IkDeKDMaVCgGpq52Pfju9WwjYc66qPbMDbHA39u3qMs");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/stampdetail/index?id=%s", rid));
            requestCopyRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一份用章抄送");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "用章申请");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "新发起");
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(request.getCreateTime()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            requestCopyRemind.put("data", data);
            mpUserService.pushTemplateMessage(requestCopyRemind, requestMemberEntity.getUid(), "request");
        }

    }

    @Override
    @Transactional
    public void update(RequestEntity request) {
        this.updateAllColumnById(request);//全部更新
        this.saveOrUpdateExtra(request);
    }

    @Override
    public RequestEntity info(Long id, UserEntity userEntity) {
        RequestEntity request = this.selectById(id);
        if (userEntity != null) {
            this.packageExtra(request, true, userEntity.getId());
        } else {
            this.packageExtra(request, true, null);
        }
        return request;
    }

    @Override
    public int getCount(RequestQueryParam requestQueryParam) {
        return this.baseMapper.getCount(requestQueryParam);
    }

    /**
     * 审核通过/拒绝
     * @param requestVerifyForm
     */
    @Override
    @Transactional
    public void verify(RequestVerifyForm requestVerifyForm) {
        RequestEntity requestEntity = this.selectById(requestVerifyForm.getRequestId());
        Assert.isNull(requestEntity, "当前审批的用章不存在");
        //状态 0审核中 1审核通过 2撤销 3未通过

        RequestMemberEntity requestMemberEntity = requestMemberService.getByRidUid(requestVerifyForm.getRequestId(), requestVerifyForm.getUid());
        Assert.isNull(requestMemberEntity, "当前用户无审核的权限");
        if (requestMemberEntity.getStatus().equals(RequestMemberVerifyStatusEnum.未审核.getCode())){
            requestMemberEntity.setStatus(requestVerifyForm.getRequestMemberVerifyStatusEnum().getCode());
            requestMemberEntity.setVerifyTime(new Date());
            requestMemberEntity.setVerifyUrl(requestVerifyForm.getVerifyUrl());
            requestMemberService.updateById(requestMemberEntity);
            //数量判断，更新用章申请状态
            this.innerChangeRequestStatus(requestEntity, requestMemberEntity.getStatus());
        }else{
            throw new RRException("已审核过，请勿重复审核");
        }

        /*if (requestEntity.getStatus().equals(RequestStatusEnum.审核中.getCode())){

        }else{
            throw new RRException("当前状态不能审核");
        }*/
    }

    /**
     * 数量判断，更新用章申请状态
     * @param requestEntity
     */
    private void innerChangeRequestStatus(RequestEntity requestEntity, Integer currStatus){
        int beforeStatus = requestEntity.getStatus();
        //获取审核通过数量是否过半，如果过半则更改用章申请表状态
        RequestMemberQueryParam requestMemberQueryParam = new RequestMemberQueryParam();
        requestMemberQueryParam.setRequestMemberType(RequestOpeTypeEnum.审批.getCode());
        requestMemberQueryParam.setRequestId(requestEntity.getId());
        //需要审核总数量
        int totalCount = requestMemberService.getCount(requestMemberQueryParam);
        List<Integer> requestMemberVerifyStatusList = new ArrayList<>();
        requestMemberVerifyStatusList.add(RequestMemberVerifyStatusEnum.通过.getCode());
        requestMemberVerifyStatusList.add(RequestMemberVerifyStatusEnum.超时同意.getCode());
        requestMemberQueryParam.setRequestMemberVerifyStatusList(requestMemberVerifyStatusList);
        //审核通过数量
        int verifiedCount = requestMemberService.getCount(requestMemberQueryParam);
        requestMemberQueryParam.setRequestMemberVerifyStatus(RequestMemberVerifyStatusEnum.未通过.getCode());
        requestMemberQueryParam.setRequestMemberVerifyStatusList(null);
        //审核拒绝数量
        int rejectCount = requestMemberService.getCount(requestMemberQueryParam);
        logger.info("innerChangeRequestStatus totalCount:{}, verifiedCount:{}, rejectCount:{}, verifiedCount > totalCount / 2:{}, rejectCount >= totalCount / 2:{}",
                totalCount, verifiedCount, rejectCount, verifiedCount > totalCount / 2, rejectCount >= totalCount / 2);
        System.out.println(requestEntity.getVerifyType());
        if (requestEntity.getVerifyType() != null && 2 == requestEntity.getVerifyType()) {
            if (currStatus != null) {
                if (verifiedCount + rejectCount == totalCount) {
                    if (currStatus == RequestMemberVerifyStatusEnum.通过.getCode()) {
                        requestEntity.setStatus(RequestStatusEnum.审核通过.getCode());
                    } else if (currStatus == 2 || currStatus == 3) {
                        requestEntity.setStatus(RequestStatusEnum.未通过.getCode());
                    }
                }
            }
        } else if (totalCount%2!=0){//奇数
            if (verifiedCount >= (totalCount+1) / 2){
                //如果过半的话审批通过
                requestEntity.setStatus(RequestStatusEnum.审核通过.getCode());
            }else if (rejectCount >= (totalCount+1) / 2){
                //如果过半的话审批拒绝
                requestEntity.setStatus(RequestStatusEnum.未通过.getCode());
            }
        }else{//偶数
            if (verifiedCount > totalCount / 2){
                //如果过半的话审批通过
                requestEntity.setStatus(RequestStatusEnum.审核通过.getCode());
            }else if (rejectCount >= totalCount / 2){
                //如果过半的话审批拒绝
                requestEntity.setStatus(RequestStatusEnum.未通过.getCode());
            }
        }
        this.updateById(requestEntity);
        logger.info("innerChangeRequestStatus requestEntity:{}", StrUtils.toJson(requestEntity));

        Long rid =requestEntity.getId();
//        Long vid = 232L; //测试

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (beforeStatus == RequestStatusEnum.审核中.getCode() && requestEntity.getStatus() != RequestStatusEnum.审核中.getCode()) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", requestEntity.getUid()));
            if (mpUserEntity == null) return;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject requestRemind = new JSONObject();
            requestRemind.put("touser", openId);
            requestRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/stampdetail/index?id=%s", rid));
            requestRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您发起的用章申请结果已产生");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "用章申请");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", RequestStatusEnum.getName(requestEntity.getStatus()));
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(new Date()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            requestRemind.put("data", data);
            mpUserService.pushTemplateMessage(requestRemind, requestEntity.getUid(), "request");
        }
    }

    /**
     * 获取出超过24小时，审核未完成的默认审核通过；并更新用章状态
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void expireRequest() {
        RequestQueryParam requestQueryParam = new RequestQueryParam();
        requestQueryParam.setExpireTime(new Date());
        List<Integer> requestStatusList = new ArrayList<>();
        requestStatusList.add(RequestStatusEnum.审核通过.getCode());
        requestStatusList.add(RequestStatusEnum.审核中.getCode());
        requestStatusList.add(RequestStatusEnum.未通过.getCode());
        requestQueryParam.setRequestStatusList(requestStatusList);
//        requestQueryParam.setMaxExpireTime(DateUtil.offset(new Date(), DateField.HOUR, -48));
//        requestQueryParam.setRequestStatus(RequestStatusEnum.审核中.getCode());
        //获取出超过24小时，审核未完成的用章申请
        List<RequestEntity> requestEntityList = this.simpleList(requestQueryParam);
//        logger.info("获取出超过24小时，审核未完成的用章申请:{}", StrUtils.toJson(requestEntityList));
        if(null == requestEntityList && requestEntityList.size()==0){
            return;
        }
        for (RequestEntity requestEntity : requestEntityList) {
            List<RequestMemberEntity> requestMemberEntityList = requestMemberService
                    .listByRequestId(requestEntity.getId(), RequestMemberVerifyStatusEnum.未审核.getCode(), RequestOpeTypeEnum.审批.getCode());
            logger.info("获取出超过24小时，审核未完成的用章申请的需要变成超时同意的操作记录:{}", StrUtils.toJson(requestMemberEntityList));
            if (requestMemberEntityList == null || requestMemberEntityList.size() == 0) {
                continue;
            }
            for (RequestMemberEntity requestMemberEntity : requestMemberEntityList) {
                //默认超时通过
                if (requestEntity.getVerifyType() == 2) {
                    requestMemberEntity.setStatus(RequestMemberVerifyStatusEnum.未通过.getCode());
                } else {
                    requestMemberEntity.setStatus(RequestMemberVerifyStatusEnum.超时同意.getCode());
                }
            }
            requestMemberService.updateBatchById(requestMemberEntityList);
            //数量判断，更新用章申请状态
            this.innerChangeRequestStatus(requestEntity, null);
        }
    }

    @Override
    public List<RequestEntity> simpleList(RequestQueryParam requestQueryParam) {
        return this.baseMapper.simpleList(requestQueryParam);
    }

    /**
     * 审核撤销
     * @param requestCancelForm
     */
    @Override
    public void requestCancel(RequestCancelForm requestCancelForm) {
        RequestEntity requestEntity = this.selectById(requestCancelForm.getRid());
        Assert.isNull(requestEntity, "当前用章申请不存在");
        if (!requestEntity.getUid().equals(requestCancelForm.getUid())){
            throw new RRException("非用章发起用户，不能撤销用章申请");
        }
        if (!requestEntity.getStatus().equals(RequestStatusEnum.审核中.getCode())){
            throw new RRException("当前审核状态为"+ RequestStatusEnum.getName(requestEntity.getStatus()) +"不能进行撤销");
        }
        requestEntity.setStatus(RequestStatusEnum.撤销.getCode());
        this.updateById(requestEntity);
    }


    /**
     * 保存更新多余信息看；
     *
     * @param request
     */
    private void saveOrUpdateExtra(RequestEntity request){
        //删除关联的文件
        multimediaService.delete(new EntityWrapper<MultimediaEntity>()
                .eq("related_id", request.getId())
                .eq("related_type", BizTypeEnum.REQUEST.getCode())
                .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        //并插入关联的文件
        List<FileEntity> fileEntityList =  request.getFileList();
        if (null != fileEntityList && fileEntityList.size()>0){
            for (FileEntity fileEntity : fileEntityList) {
//                urlList.add(fileEntity.getResponse().getUrl());
                //保存图片
                MultimediaEntity multimediaEntity = new MultimediaEntity();
                multimediaEntity.setRelatedType(BizTypeEnum.REQUEST.getCode());
                multimediaEntity.setRelatedId(request.getId());
                multimediaEntity.setFileType(FileTypeEnum.PICTURE.getCode());
                if(null != fileEntity.getResponse()){
                    multimediaEntity.setUrl(fileEntity.getResponse().getUrl());
                }else{
                    multimediaEntity.setUrl(fileEntity.getUrl());
                }
                multimediaEntity.setName(fileEntity.getName());
                multimediaService.insert(multimediaEntity);
            }
        }

    }

    /**
     * 封装
     * @param request
     * @param uid
     */
    private void packageExtra(RequestEntity request, Boolean isInfo, Long uid){
        //是否详情
        if(isInfo){
            List<FileEntity> fileEntityList = new ArrayList<>();
            List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
                    new EntityWrapper<MultimediaEntity>()
                            .eq("related_id", request.getId())
                            .eq("related_type", BizTypeEnum.REQUEST.getCode())
                            .eq("file_type", FileTypeEnum.PICTURE.getCode())
            );
            for (MultimediaEntity multimediaEntity : multimediaEntityList) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setName(multimediaEntity.getName());
                fileEntity.setUrl(multimediaEntity.getUrl());
                fileEntityList.add(fileEntity);
            }
            request.setFileList(fileEntityList);

            //审批列表
            List<RequestMemberEntity> verifyMemberEntityList = requestMemberService.listByRequestId(request.getId(), null, RequestOpeTypeEnum.审批.getCode());
            request.setVerifyMemberEntityList(verifyMemberEntityList);
            //抄送列表
            List<RequestMemberEntity> copyToMemberEntityList = requestMemberService.listByRequestId(request.getId(), null, RequestOpeTypeEnum.抄送.getCode());
            request.setCopyToMemberEntityList(copyToMemberEntityList);
        }

        request.setStatusCn(RequestStatusEnum.getName(request.getStatus()));
        UserEntity userEntity = userService.selectById(request.getUid());
        if (null != userEntity){
            request.setUname(userEntity.getRealname());
            request.setAvatarUrl(userEntity.getAvatarUrl());
        }
        request.setFileTypeCn(RequestFileTypeEnum.getName(request.getFileType()));
        request.setSealCn(RequestSealEnum.getName(request.getSeal()));
        request.setPrintDate(new Date());

        if (uid != null){
            RequestMemberEntity requestMemberEntity = requestMemberService.getByRidUid(request.getId(), uid);
            if (requestMemberEntity != null){
                request.setMemberStatus(requestMemberEntity.getStatus());
                request.setMemberStatusCn(RequestMemberVerifyStatusEnum.getName(requestMemberEntity.getStatus()));
            }
        }

        if (request.getZoneId()!=null){
            ZonesEntity zonesEntity = zonesService.selectById(request.getZoneId());
            if (null != zonesEntity){
                request.setZoneName(zonesEntity.getName());
            }
        }

    }

}