package com.yeweihui.modules.operation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.common.service.MultimediaService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.dao.HisViewLogDao;
import com.yeweihui.modules.operation.entity.HisViewLogEntity;
import com.yeweihui.modules.operation.service.*;
import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.entity.SysUserRoleEntity;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service("hisViewLogService")
public class HisViewLogServiceImpl extends ServiceImpl<HisViewLogDao, HisViewLogEntity> implements HisViewLogService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    MultimediaService multimediaService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    private ZonesService zonesService;

    @Autowired
    private BillService billService;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private AnnounceService announceService;

    private ExecutorService asyncSavePool = new ThreadPoolExecutor(10, 100, 5L, TimeUnit.MINUTES,
            new LinkedBlockingDeque<Runnable>(10000));


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
        List<HisViewLogEntity> list = this.baseMapper.selectPageEn(page, params);
        for (HisViewLogEntity hisViewLogEntity:list) {
            this.packageExtra(hisViewLogEntity, false, viewUid);
        }

        page.setRecords(list);

        return new PageUtils(page);
    }

    @Override
    public HisViewLogEntity info(Long id, UserEntity userEntity) {
        HisViewLogEntity hisViewLogEntity = this.selectById(id);
        this.packageExtra(hisViewLogEntity, true, userEntity == null ? null : userEntity.getId());
        return hisViewLogEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(BizTypeEnum bizTypeEnum, UserEntity userEntity, Long referId) {
        HisViewLogEntity hisViewLogEntity = new HisViewLogEntity();
        if (null == userEntity) {
            logger.warn("user is empty. biz={}, referId={}", bizTypeEnum.getName(), referId);
            return;
        }

        hisViewLogEntity.setType(bizTypeEnum.getCode());
        hisViewLogEntity.setReferId(referId);
        hisViewLogEntity.setUid(userEntity.getId());
        hisViewLogEntity.setZoneId(userEntity.getZoneId());

        asyncSavePool.execute(() -> {
            try {
                this.baseMapper.insert(hisViewLogEntity);
            } catch (Exception e) {
                logger.error("save his_view_log failed. entity={}", JSON.toJSONString(hisViewLogEntity), e);
            }
        });
    }

    /**
     * 封装
     * @param hisViewLogEntity
     * @param isInfo
     * @param uid
     */
    private void packageExtra(HisViewLogEntity hisViewLogEntity, boolean isInfo, Long uid){
        if (!isInfo) {
            return;
        }

        if (hisViewLogEntity.getZoneId() != null) {
            ZonesEntity zonesEntity = zonesService.getById(hisViewLogEntity.getZoneId());
            if (zonesEntity != null) {
                hisViewLogEntity.setZoneName(zonesEntity.getName());
            }
        }

        UserEntity userEntity = null;
        // 补充记录查看人的实体
        if (hisViewLogEntity.getUid() != null) {
            userEntity = userService.info(hisViewLogEntity.getUid());
            if (userEntity != null) {
                hisViewLogEntity.setUserEntity(userEntity);
                hisViewLogEntity.setRealname(userEntity.getRealname());
            }
        }

        // 补充记录本身的实体
        BizTypeEnum bizTypeEnum = BizTypeEnum.getEnumByCode(hisViewLogEntity.getType());
        Long referId = hisViewLogEntity.getReferId();
        Object record = null;
        if (bizTypeEnum != null && referId != null) {
            switch (bizTypeEnum) {
                case BILL: record = billService.info(referId, userEntity);break;
                case MEETING: record = meetingService.info(referId, userEntity);break;
                case PAPER: record = paperService.info(referId, userEntity);break;
                case REQUEST: record = requestService.info(referId, userEntity);break;
                case VOTE: record = voteService.info(referId, userEntity);break;
                case NOTICE: record = noticeService.info(referId, userEntity);break;
                case Announcement: record = announceService.info(referId, userEntity);break;
                default: {
                    logger.error("没有找到对应的业务类型! entity={}", JSON.toJSONString(hisViewLogEntity));
                }
            }
        }
        hisViewLogEntity.setRecord(record);

//        List<FileEntity> fileEntityList = new ArrayList<>();
//        List<MultimediaEntity> multimediaEntityList = multimediaService.selectList(
//                new EntityWrapper<MultimediaEntity>()
//                        .eq("related_id", hisViewLogEntity.getId())
//                        .eq("related_type", BizTypeEnum.BILL.getCode())
//                        .eq("file_type", FileTypeEnum.PICTURE.getCode())
//        );
//        for (MultimediaEntity multimediaEntity : multimediaEntityList) {
//            FileEntity fileEntity = new FileEntity();
//            fileEntity.setName(multimediaEntity.getName());
//            fileEntity.setUrl(multimediaEntity.getUrl());
//            fileEntityList.add(fileEntity);
//        }
//        hisViewLogEntity.setFileList(fileEntityList);
//
//        //状态 0等待 1通过 2未通过
//        hisViewLogEntity.setStatusCn(BillStatusEnum.getName(hisViewLogEntity.getStatus()));
//
//        //发起用户姓名 realname
//        UserEntity userEntity = userService.selectById(hisViewLogEntity.getUid());
//        if (userEntity != null){
//            hisViewLogEntity.setRealname(userEntity.getRealname());
//            hisViewLogEntity.setAvatarUrl(userEntity.getAvatarUrl());
//        }
//
//        //审核状态 statusCn
//        // 费用发生 happenDate
//        // 申请时间 createTime
//        // 报销金额 money
//        // 详情说明 content
//        // 图片 fileList 上面
//        //审批列表
//        List<BillMemberEntity> verifyMemberEntityList = billMemberService.listByBillId(hisViewLogEntity.getId());
//        hisViewLogEntity.setVerifyMemberEntityList(verifyMemberEntityList);
//        //抄送人员列表
//        BillMemberQueryParam billMemberQueryParam  = new BillMemberQueryParam();
//        billMemberQueryParam.setBid(hisViewLogEntity.getId());
//        billMemberQueryParam.setBillMemberType(BillMemberTypeEnum.抄送.getCode());
//        List<BillMemberEntity> copyToMemberEntityList = billMemberService.simpleList(billMemberQueryParam);
//        hisViewLogEntity.setCopyToMemberEntityList(copyToMemberEntityList);
//        // 审批人  审批状态
//        // 业委会盖公章处
//        // 打印日期:   2019-10-09 19:56
//        hisViewLogEntity.setPrintDate(new Date());
//
//        if (null != uid){
//            BillMemberEntity billMemberEntity = billMemberService.getByBidUid(hisViewLogEntity.getId(), uid, BillMemberTypeEnum.审批.getCode());
//            if (billMemberEntity != null){
//                hisViewLogEntity.setMemberStatus(billMemberEntity.getStatus());
//                hisViewLogEntity.setMemberStatusCn(BillMemberStatusEnum.getName(billMemberEntity.getStatus()));
//            }
//        }
//
//        if (hisViewLogEntity.getZoneId()!=null){
//            ZonesEntity zonesEntity = zonesService.selectById(hisViewLogEntity.getZoneId());
//            if (null != zonesEntity){
//                hisViewLogEntity.setZoneName(zonesEntity.getName());
//            }
//        }

    }

}
