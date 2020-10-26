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
import com.yeweihui.modules.enums.bill.BillMemberStatusEnum;
import com.yeweihui.modules.enums.bill.BillMemberTypeEnum;
import com.yeweihui.modules.enums.bill.BillStatusEnum;
import com.yeweihui.modules.enums.bill.BillTypeEnum;
import com.yeweihui.modules.enums.bill.BillMemberTypeEnum;
import com.yeweihui.modules.enums.bill.BillStatusEnum;
import com.yeweihui.modules.operation.dao.BillDao;
import com.yeweihui.modules.operation.entity.BillEntity;
import com.yeweihui.modules.operation.entity.BillMemberEntity;
import com.yeweihui.modules.operation.entity.BillMemberEntity;
import com.yeweihui.modules.operation.service.BillMemberService;
import com.yeweihui.modules.operation.service.BillService;
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
import com.yeweihui.modules.vo.api.form.BillVerifyForm;
import com.yeweihui.modules.vo.query.BillMemberQueryParam;
import com.yeweihui.modules.vo.query.BillQueryParam;
import com.yeweihui.modules.vo.query.UserQueryParam;
import com.yeweihui.modules.vo.query.BillMemberQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service("billService")
public class BillServiceImpl extends ServiceImpl<BillDao, BillEntity> implements BillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    MultimediaService multimediaService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    BillMemberService billMemberService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private MpUserService mpUserService;

    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
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
        List<BillEntity> list = this.baseMapper.selectPageEn(page, params);
        for (BillEntity bill:list) {
            this.packageExtra(bill, false, viewUid);
        }

        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    public BillEntity info(Long id, UserEntity userEntity) {
        BillEntity bill = this.selectById(id);
        this.packageExtra(bill, true, userEntity == null ? null : userEntity.getId());
        return bill;
    }

    @Override
    @Transactional
    public void save(BillEntity bill) {
        UserEntity userEntity = userService.selectById(bill.getUid());
        if (null != userEntity){
            bill.setName(userEntity.getRealname());
            bill.setZoneId(userEntity.getZoneId());
        }
        //todo: 判断是大金额还是小金额的这个金额每个小区到时候设置一下
        List<BillMemberEntity> billMemberEntityList = new ArrayList<>();
        if (bill.getType().equals(BillTypeEnum.小额主任审批.getCode())){//小于1k
//        if (bill.getMoney().compareTo(new BigDecimal(1000)) <= 0){//小于1k
            bill.setType(BillTypeEnum.小额主任审批.getCode());

            this.insert(bill);

            //获取主任角色，获取当前小区主任用户
            UserEntity director = userService.getDirectorByZoneId(bill.getZoneId());
            Assert.isNull(director, "当前业委会没有主任");

            BillMemberEntity billMemberEntity = new BillMemberEntity();
            billMemberEntity.setBid(bill.getId());
            billMemberEntity.setReferUid(bill.getUid());
            billMemberEntity.setUid(director.getId());
            billMemberEntity.setType(BillMemberTypeEnum.审批.getCode());
            billMemberService.insert(billMemberEntity);
            billMemberEntityList.add(billMemberEntity);
        }else if (bill.getType().equals(BillTypeEnum.大额业委会审批.getCode())){//大于1k
//        }else if (bill.getMoney().compareTo(new BigDecimal(1000)) > 0){//大于1k
            bill.setType(BillTypeEnum.大额业委会审批.getCode());

            //获取当前角色组的角色roleCodeList
            List<String> roleCodeList = sysRoleService.getRoleCodeByGroup("业主委员会");
            //根据小区id和角色列表获取用户人员列表
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setZoneId(bill.getZoneId());
            userQueryParam.setRoleCodeList(roleCodeList);
            List<UserEntity> userEntityList = userService.simpleList(userQueryParam);
            //需要审批的总人数是业主委员会的全体成员
            bill.setTotal(userEntityList.size());

            this.insert(bill);

            for (int i = 0; i < userEntityList.size(); i++) {
                UserEntity userEntity1 = userEntityList.get(i);
                BillMemberEntity billMemberEntity = new BillMemberEntity();
                billMemberEntity.setBid(bill.getId());
                billMemberEntity.setReferUid(bill.getUid());
                billMemberEntity.setUid(userEntity1.getId());
                billMemberEntity.setType(BillMemberTypeEnum.审批.getCode());
                billMemberService.insert(billMemberEntity);
                billMemberEntityList.add(billMemberEntity);
            }
        }

        List<BillMemberEntity> copyToMemberEntityList = bill.getCopyToMemberEntityList();
        if(copyToMemberEntityList != null && copyToMemberEntityList.size()>0){
            for (BillMemberEntity billMemberEntity : copyToMemberEntityList) {
                billMemberEntity.setBid(bill.getId());
                billMemberEntity.setReferUid(bill.getUid());
                billMemberEntity.setType(BillMemberTypeEnum.抄送.getCode());
                billMemberService.insert(billMemberEntity);
            }
        }

        Long bid = bill.getId();
        UserEntity refUser = userService.selectById(bill.getUid());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (BillMemberEntity billMemberEntity: billMemberEntityList) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", billMemberEntity.getUid()));
            if (mpUserEntity == null) continue;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject billRemind = new JSONObject();
            billRemind.put("touser", openId);
            billRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/billdetail/index?id=%s", bid));
            billRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您有一项新的费用报销审批");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "费用报销");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", refUser.getRealname());
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(bill.getCreateTime()));
            JSONObject keyword4 = new JSONObject();
            keyword4.put("value", sysRoleService.getHighestLevelRoleNameByUserId(userEntity.getId()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            data.put("keyword4", keyword4);
            billRemind.put("data", data);
            System.out.println(billRemind);
            mpUserService.pushTemplateMessage(billRemind, billMemberEntity.getUid(), "bill");
        }

        new Timer().schedule(new TimerTask() {

            int i=0;
            public void run() {

                i = i+1;
                if (i>=3)return;

                List<BillMemberEntity> copyToMemberEntityList = bill.getCopyToMemberEntityList();
                if(copyToMemberEntityList != null && copyToMemberEntityList.size()>0){
                    for (BillMemberEntity billMemberEntity : copyToMemberEntityList) {
                        billMemberEntity.setBid(bill.getId());
                        billMemberEntity.setReferUid(bill.getUid());
                        billMemberEntity.setType(BillMemberTypeEnum.抄送.getCode());
                        billMemberService.insert(billMemberEntity);
                    }
                }

                Long bid = bill.getId();
                UserEntity refUser = userService.selectById(bill.getUid());

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                for (BillMemberEntity billMemberEntity: billMemberEntityList) {
                    MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", billMemberEntity.getUid()));
                    if (mpUserEntity == null) continue;
                    String openId = mpUserEntity.getOpenidPublic();
                    JSONObject billRemind = new JSONObject();
                    billRemind.put("touser", openId);
                    billRemind.put("template_id", "FO8_aLmkgrWVtM9yy6AA0wTi8gjeD4wpY9-26Txm0NI");
                    JSONObject miniProgram = new JSONObject();
                    miniProgram.put("appid", "wx5e7524c02dade60a");
                    miniProgram.put("pagepath", String.format("pages/billdetail/index?id=%s", bid));
                    billRemind.put("miniprogram", miniProgram);
                    JSONObject data = new JSONObject();
                    JSONObject first = new JSONObject();
                    first.put("value", "您有一项新的费用报销审批");
                    JSONObject keyword1 = new JSONObject();
                    keyword1.put("value", "费用报销");
                    JSONObject keyword2 = new JSONObject();
                    keyword2.put("value", refUser.getRealname());
                    JSONObject keyword3 = new JSONObject();
                    keyword3.put("value", df.format(bill.getCreateTime()));
                    JSONObject keyword4 = new JSONObject();
                    keyword4.put("value", userEntity.getRoleName());
                    data.put("first", first);
                    data.put("keyword1", keyword1);
                    data.put("keyword2", keyword2);
                    data.put("keyword3", keyword3);
                    data.put("keyword4", keyword4);
                    billRemind.put("data", data);
                    System.out.println(billRemind);
                    mpUserService.pushTemplateMessage(billRemind, billMemberEntity.getUid(), "bill");
                }

            }
        }, 1000*60 , 1000);


        this.saveOrUpdateExtra(bill);
    }

    @Override
    @Transactional
    public void update(BillEntity bill) {
//        if (bill.getMoney().compareTo(new BigDecimal(1000)) < 0){//小于1k
        if (bill.getType().equals(BillTypeEnum.小额主任审批.getCode())){//小于1k
            bill.setType(BillTypeEnum.小额主任审批.getCode());
            bill.setTotal(1);

            this.updateAllColumnById(bill);//全部更新

            //根据报销id删除之前需要审批的人员
            billMemberService.deleteByBillId(bill.getId());

            //获取主任角色，获取当前小区主任用户
            UserEntity director = userService.getDirectorByZoneId(bill.getZoneId());
            Assert.isNull(director, "当前业委会没有主任");

            BillMemberEntity billMemberEntity = new BillMemberEntity();
            billMemberEntity.setBid(bill.getId());
            billMemberEntity.setReferUid(bill.getUid());
            billMemberEntity.setUid(director.getId());
            billMemberEntity.setType(BillMemberTypeEnum.审批.getCode());
            billMemberService.insert(billMemberEntity);
//        }else if (bill.getMoney().compareTo(new BigDecimal(1000)) > 0){//大于1k
        }else if (bill.getType().equals(BillTypeEnum.大额业委会审批.getCode())){
            bill.setType(BillTypeEnum.大额业委会审批.getCode());

            //获取当前角色组的角色roleCodeList
            List<String> roleCodeList = sysRoleService.getRoleCodeByGroup("业主委员会");
            //根据小区id和角色列表获取用户人员列表
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setZoneId(bill.getZoneId());
            userQueryParam.setRoleCodeList(roleCodeList);
            List<UserEntity> userEntityList = userService.simpleList(userQueryParam);
            //需要审批的总人数是业主委员会的全体成员
            bill.setTotal(userEntityList.size());

            this.updateAllColumnById(bill);//全部更新

            //根据报销id删除之前需要审批的人员
            billMemberService.deleteByBillId(bill.getId());

            for (int i = 0; i < userEntityList.size(); i++) {
                UserEntity userEntity1 = userEntityList.get(i);
                BillMemberEntity billMemberEntity = new BillMemberEntity();
                billMemberEntity.setBid(bill.getId());
                billMemberEntity.setReferUid(bill.getUid());
                billMemberEntity.setUid(userEntity1.getId());
                billMemberEntity.setType(BillMemberTypeEnum.审批.getCode());
                billMemberService.insert(billMemberEntity);
            }
        }

        this.saveOrUpdateExtra(bill);
    }

    @Override
    public int getCount(BillQueryParam billQueryParam) {
        return this.baseMapper.getCount(billQueryParam);
    }

    /**
     * 通过拒绝报销审批
     * @param billVerifyForm
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void verify(BillVerifyForm billVerifyForm) {
        BillEntity billEntity = this.selectById(billVerifyForm.getBid());
        Assert.isNull(billEntity, "费用报销不存在，请检查后重试");
        /*if(billEntity.getStatus().equals(BillStatusEnum.通过.getCode())
            || billEntity.getStatus().equals(BillStatusEnum.未通过.getCode())){
            throw new RRException("当前状态费用报销不能审核");
        }*/

        BillMemberEntity billMemberEntity = billMemberService.getByBidUid(billVerifyForm.getBid(), billVerifyForm.getUid(), BillMemberTypeEnum.审批.getCode());
        Assert.isNull(billMemberEntity, "当前用户无法对该费用报销进行审批");
        billMemberEntity.setStatus(billVerifyForm.getBillMemberStatusEnum().getCode());
        billMemberEntity.setVerifyTime(new Date());
        billMemberEntity.setVerifyUrl(billVerifyForm.getVerifyUrl());
        billMemberService.updateById(billMemberEntity);

        BillMemberQueryParam billMemberQueryParam = new BillMemberQueryParam();
        billMemberQueryParam.setBid(billVerifyForm.getBid());
        billMemberQueryParam.setBillMemberType(BillMemberTypeEnum.审批.getCode());
        int totalCount = billMemberService.getCount(billMemberQueryParam);
        billMemberQueryParam.setBillMemberStatus(BillMemberStatusEnum.同意.getCode());
        int verifiedCount = billMemberService.getCount(billMemberQueryParam);
        billMemberQueryParam.setBillMemberStatus(BillMemberStatusEnum.不同意.getCode());
        int rejectedCount = billMemberService.getCount(billMemberQueryParam);
        //如果审批的人已经过半数

        logger.info("bill verify totalCount:{}, verifiedCount:{}, rejectedCount:{}",
                totalCount, verifiedCount, rejectedCount);
        int beforeStatus = billEntity.getStatus();
        if (totalCount%2!=0){//奇数
            logger.info("verifiedCount >= (totalCount+1) / 2:{}", verifiedCount >= (totalCount+1) / 2);
            if (verifiedCount >= (totalCount+1) / 2){
                //如果过半的话审批通过
                billEntity.setStatus(BillStatusEnum.通过.getCode());
                this.updateById(billEntity);
            }else if(rejectedCount >= (totalCount+1) / 2){
                //如果弃权和超时的过半的话审批拒绝
                billEntity.setStatus(BillStatusEnum.未通过.getCode());
                this.updateById(billEntity);
            }else{
                billEntity.setStatus(BillStatusEnum.等待.getCode());
            }
        }else{//偶数
            logger.info("verifiedCount >= (totalCount+1) / 2:{}", verifiedCount > totalCount / 2);
            if (verifiedCount > totalCount / 2){
                //如果过半的话审批通过
                billEntity.setStatus(BillStatusEnum.通过.getCode());
            }else if (rejectedCount >= totalCount / 2){
                //如果过半的话审批拒绝
                billEntity.setStatus(BillStatusEnum.未通过.getCode());
            }else{
                billEntity.setStatus(BillStatusEnum.等待.getCode());
            }
        }


        /*if (verifiedCount > totalCount/2){
            billEntity.setStatus(BillStatusEnum.通过.getCode());
        }else if (rejectedCount >= (totalCount+1)/2){
            billEntity.setStatus(BillStatusEnum.未通过.getCode());
        }else{
            billEntity.setStatus(BillStatusEnum.等待.getCode());
        }*/

        billEntity.setChecked(billEntity.getChecked()+1);
        this.updateById(billEntity);
        Long bid = billEntity.getId();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (beforeStatus != BillStatusEnum.通过.getCode() && billEntity.getStatus() == BillStatusEnum.通过.getCode()) {
            MpUserEntity mpUserEntity = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", billEntity.getUid()));
            if (mpUserEntity == null) return;
            String openId = mpUserEntity.getOpenidPublic();
            JSONObject billRemind = new JSONObject();
            billRemind.put("touser", openId);
            billRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
            JSONObject miniProgram = new JSONObject();
            miniProgram.put("appid", "wx5e7524c02dade60a");
            miniProgram.put("pagepath", String.format("pages/billdetail/index?id=%s", bid));
            billRemind.put("miniprogram", miniProgram);
            JSONObject data = new JSONObject();
            JSONObject first = new JSONObject();
            first.put("value", "您发起的费用报销结果已产生");
            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "费用报销");
            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", BillStatusEnum.getName(billEntity.getStatus()));
            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", df.format(new Date()));
            data.put("first", first);
            data.put("keyword1", keyword1);
            data.put("keyword2", keyword2);
            data.put("keyword3", keyword3);
            billRemind.put("data", data);
            System.out.println(billRemind.toJSONString());
            mpUserService.pushTemplateMessage(billRemind, billEntity.getUid(), "bill");
            BillMemberQueryParam billMemberQueryParam2 = new BillMemberQueryParam();
            billMemberQueryParam.setBid(billEntity.getId());
            billMemberQueryParam.setBillMemberType(BillMemberTypeEnum.抄送.getCode());
            List<BillMemberEntity> billMemberEntityList = billMemberService.simpleList(billMemberQueryParam2);
            for (BillMemberEntity billMemberEntity2: billMemberEntityList) {
                MpUserEntity mpUserEntityMember = mpUserService.selectOne(new EntityWrapper<MpUserEntity>().eq("uid", billMemberEntity2.getUid()));
                if (mpUserEntityMember == null) return;
                openId = mpUserEntity.getOpenidPublic();
                billRemind = new JSONObject();
                billRemind.put("touser", openId);
                billRemind.put("template_id", "laZQDwIoFAgNGlnlLIHzbuqN6dyIn5ihD9-dtCxBWGA");
                miniProgram = new JSONObject();
                miniProgram.put("appid", "wx5e7524c02dade60a");
                miniProgram.put("pagepath", String.format("pages/billdetail/index?id=%s", bid));
                billRemind.put("miniprogram", miniProgram);
                data = new JSONObject();
                first = new JSONObject();
                first.put("value", "有一项费用报销的审批结果已产生");
                keyword1 = new JSONObject();
                keyword1.put("value", "费用报销");
                keyword2 = new JSONObject();
                keyword2.put("value", BillStatusEnum.getName(billEntity.getStatus()));
                keyword3 = new JSONObject();
                keyword3.put("value", df.format(new Date()));
                data.put("first", first);
                data.put("keyword1", keyword1);
                data.put("keyword2", keyword2);
                data.put("keyword3", keyword3);
                billRemind.put("data", data);
                System.out.println(billRemind.toJSONString());
                mpUserService.pushTemplateMessage(billRemind, billEntity.getUid(), "bill");
            }
        }

        /*if(billEntity.getType().equals(BillTypeEnum.大额业委会审批.getCode())){
            //大额审批需要所有人通过

        }else if(billEntity.getType().equals(BillTypeEnum.大额业委会审批.getCode())){
            //小额审批需要

        }*/
    }

    /*public static void main(String[] args) {
        int verifiedCount = 4;
        int totalCount = 8;
        int rejectedCount = 3;
        if (verifiedCount > totalCount/2){
            System.out.println("通过");
        }else if (rejectedCount >= (totalCount+1)/2){
            System.out.println("未通过");
        }else{
            System.out.println("等待");
        }
    }*/

    /**
     * 保存更新多余信息
     * @param bill
     */
    private void saveOrUpdateExtra(BillEntity bill){
        //删除关联的文件
        multimediaService.delete(new EntityWrapper<MultimediaEntity>()
                .eq("related_id", bill.getId())
                .eq("related_type", BizTypeEnum.BILL.getCode())
                .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        //并插入关联的文件
        List<FileEntity> fileEntityList =  bill.getFileList();
        if (null != fileEntityList && fileEntityList.size()>0){
            for (FileEntity fileEntity : fileEntityList) {
//                urlList.add(fileEntity.getResponse().getUrl());
                //保存图片
                MultimediaEntity multimediaEntity = new MultimediaEntity();
                multimediaEntity.setName(fileEntity.getName());
                multimediaEntity.setRelatedType(BizTypeEnum.BILL.getCode());
                multimediaEntity.setRelatedId(bill.getId());
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
     * @param bill
     * @param isInfo
     * @param uid
     */
    private void packageExtra(BillEntity bill, boolean isInfo, Long uid){
        List<FileEntity> fileEntityList = new ArrayList<>();
        List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
                new EntityWrapper<MultimediaEntity>()
                        .eq("related_id", bill.getId())
                        .eq("related_type", BizTypeEnum.BILL.getCode())
                        .eq("file_type", FileTypeEnum.PICTURE.getCode())
        );
        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName(multimediaEntity.getName());
            fileEntity.setUrl(multimediaEntity.getUrl());
            fileEntityList.add(fileEntity);
        }
        bill.setFileList(fileEntityList);

        //状态 0等待 1通过 2未通过
        bill.setStatusCn(BillStatusEnum.getName(bill.getStatus()));

        //发起用户姓名 realname
        UserEntity userEntity = userService.selectById(bill.getUid());
        if (userEntity != null){
            bill.setRealname(userEntity.getRealname());
            bill.setAvatarUrl(userEntity.getAvatarUrl());
        }

        //审核状态 statusCn
        // 费用发生 happenDate
        // 申请时间 createTime
        // 报销金额 money
        // 详情说明 content
        // 图片 fileList 上面
        //审批列表
        List<BillMemberEntity> verifyMemberEntityList = billMemberService.listByBillId(bill.getId());
        bill.setVerifyMemberEntityList(verifyMemberEntityList);
        //抄送人员列表
        BillMemberQueryParam billMemberQueryParam  = new BillMemberQueryParam();
        billMemberQueryParam.setBid(bill.getId());
        billMemberQueryParam.setBillMemberType(BillMemberTypeEnum.抄送.getCode());
        List<BillMemberEntity> copyToMemberEntityList = billMemberService.simpleList(billMemberQueryParam);
        bill.setCopyToMemberEntityList(copyToMemberEntityList);
        // 审批人  审批状态
        // 业委会盖公章处
        // 打印日期:   2019-10-09 19:56
        bill.setPrintDate(new Date());

        if (null != uid){
            BillMemberEntity billMemberEntity = billMemberService.getByBidUid(bill.getId(), uid, BillMemberTypeEnum.审批.getCode());
            if (billMemberEntity != null){
                bill.setMemberStatus(billMemberEntity.getStatus());
                bill.setMemberStatusCn(BillMemberStatusEnum.getName(billMemberEntity.getStatus()));
            }
        }

        if (bill.getZoneId()!=null){
            ZonesEntity zonesEntity = zonesService.selectById(bill.getZoneId());
            if (null != zonesEntity){
                bill.setZoneName(zonesEntity.getName());
            }
        }

    }

}
