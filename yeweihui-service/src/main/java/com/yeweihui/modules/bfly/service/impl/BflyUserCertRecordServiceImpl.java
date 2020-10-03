package com.yeweihui.modules.bfly.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.modules.CommonService;
import com.yeweihui.modules.bfly.dao.BflyRoomDao;
import com.yeweihui.modules.bfly.dao.BflyUserCertRecordDao;
import com.yeweihui.modules.bfly.dao.BflyUserDao;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.entity.BflyUserCertRecord;
import com.yeweihui.modules.bfly.service.BflyRoomService;
import com.yeweihui.modules.bfly.service.BflyUserCertRecordService;
import com.yeweihui.modules.bfly.service.BflyUserService;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import static com.yeweihui.modules.enums.bfly.MagicEnum.*;

@Service
public class BflyUserCertRecordServiceImpl extends ServiceImpl<BflyUserCertRecordDao, BflyUserCertRecord> implements BflyUserCertRecordService {

    @Resource
    BflyUserCertRecordDao bflyUserCertRecordDao;
    @Resource
    BflyUserDao bflyUserDao;
    @Resource
    BflyRoomService bflyRoomService;
    @Resource
    ZonesService zonesService;
    @Resource
    BflyUserService bflyUserService;
    @Resource
    CommonService commonService;
    @Resource
    BflyRoomDao bflyRoomDao;

    @Override
    public HashMap<String,Object> selectUserCertList(Long zoneId, String ownerName, String phone, Integer page, Integer size,  Integer submitMethod) {
        Integer current = size * (page - 1);
        List<BflyUserCertRecord> bflyUserCertRecords = bflyUserCertRecordDao.selectUserCertList(ownerName, phone, zoneId, current, size, submitMethod);
        Long totalCount = bflyUserCertRecordDao.selectUserCertListCount(ownerName, phone, zoneId, submitMethod);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", bflyUserCertRecords);
        resultMap.put("totalCount", totalCount);
        resultMap.put("totalPage", (totalCount + size - 1)/size);
        resultMap.put("pageSize", size);
        resultMap.put("currPage", page);
        return resultMap;
    }

    /**
     * @Description: 后台审核用户提交的审核记录
     * @Param:  userCertId  bfly_user_cert_record表id
     * @Param:  cert  审核是否通过
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/16
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void certOwner(Long userCertId, Boolean cert) {
        BflyUserCertRecord certRecord = this.selectById(userCertId);
        BflyUser bflyUser = bflyUserDao.selectById(certRecord.getBflyUserId());
        bflyUser.setLastCertTime(new Timestamp(System.currentTimeMillis()));
        BflyRoom bflyRoom = null;
        if (cert){
            Long zoneId = bflyRoomService.selectById(certRecord.getBflyRoomId()).getZoneId();
            Integer isCertified = bflyRoomDao.isCertified(
                    zoneId,
                    certRecord.getCourt(),
                    certRecord.getBuilding(),
                    certRecord.getUnitName(),
                    certRecord.getFloorName()
            );
            if (isCertified > 0) {
                throw new RRException("该房屋已有认证业主，每户仅限认证一位，请先到“已认证查询”中对该户进行“撤销认证”，再来审核。");
            }
            bflyRoom = bflyRoomService.selectById(certRecord.getBflyRoomId());
            certRecord.setStatus((Integer) 审核通过.getCode());
            ZonesEntity zonesEntity = zonesService.selectById(bflyRoom.getZoneId());
            String address = zonesEntity.getName();
            if (StringUtils.isNotEmpty(certRecord.getCourt())) {
                address += certRecord.getCourt();
            }
            if (StringUtils.isNotEmpty(certRecord.getBuilding())) {
                if (certRecord.getBuilding().equals(SHOP.getName()) || certRecord.getBuilding().equals(HOUSE.getName())) {
                    address += certRecord.getBuilding();
                } else {
                    address += certRecord.getBuilding() + "幢";
                }
            }
            if (StringUtils.isNotEmpty(certRecord.getUnitName())) {
                address += certRecord.getUnitName() + "单元";
            }
            if (StringUtils.isNotEmpty(certRecord.getFloorName())) {
                address += certRecord.getFloorName();
            }
            bflyUser.setZoneId(bflyRoom.getZoneId());
            bflyUser.setBflyRoomId(certRecord.getBflyRoomId());
            bflyUser.setUsername(certRecord.getCheckUserName());
            bflyUser.setPhoneNum(certRecord.getCheckPhoneNum());
            bflyUser.setIdCard(certRecord.getCheckIdCard());
            bflyUser.setHousingArea(certRecord.getCheckHousingArea());
            bflyUser.setHeaderUrl(certRecord.getCheckHeaderUrl());
            bflyUser.setAddress(address);
            bflyUser.setHouseCertificateUrl(certRecord.getCheckHouseCertificateUrl());
            bflyUser.setHouseCertificateUrlExtra1(certRecord.getCheckHouseCertificateUrlExtra1());
            bflyUser.setIdCardUrl(certRecord.getCheckIdCardUrl());
            bflyUser.setIdCardUrlExtra1(certRecord.getCheckIdCardUrlExtra1());
            bflyUser.setStatus((Integer) 审核通过.getCode());
            bflyUser.setIsValid(1);
        } else {
            certRecord.setStatus((Integer)审核未通过.getCode());
            bflyUser.setStatus((Integer)审核未通过.getCode());
            bflyUser.setIsValid(0);
        }
        bflyUserCertRecordDao.updateById(certRecord);
        bflyUserDao.updateById(bflyUser);

        //审核通过，修改业主导入中的信息，并发送审核短信给申请人
        //审核未通过，发送审核未通过短信给申请人
        if (bflyUser.getStatus().equals(审核通过.getCode())) {
            //同步修改同一房屋下，业主导入信息中的的房屋面积
            BflyRoom updateArea = new BflyRoom();
            updateArea.setHousingArea(bflyUser.getHousingArea());
            bflyRoomService.update(updateArea,
                    new EntityWrapper<BflyRoom>()
                            .eq("zone_id", bflyUser.getZoneId())
                            .eq("court", certRecord.getCourt())
                            .eq("building", certRecord.getBuilding())
                            .eq("unit_name", certRecord.getUnitName())
                            .eq("floor_name", certRecord.getFloorName())
            );
            //同步修改对应业主导入信息中的所有信息
            bflyRoom.setHousingArea(bflyUser.getHousingArea());
            bflyRoom.setPhoneNum(bflyUser.getPhoneNum());
            bflyRoom.setIdCard(bflyUser.getIdCard());
            bflyRoom.setUserName(bflyUser.getUsername());
            bflyRoom.setLastCertificationTime(new Timestamp(System.currentTimeMillis()));
            bflyRoomService.updateById(bflyRoom);

//        * * 根据模板id给指定的手机号发送短信
//        * 通知管理员进行认证模版：SMS_191818114
//        * 通知业主认证通过模版：SMS_191818116
//        * 通知业主认证失败模版：SMS_191833074
            commonService.sendMsg(certRecord.getCheckPhoneNum(), "SMS_191818116");
        }
        if (bflyUser.getStatus().equals(审核未通过.getCode()))
            commonService.sendMsg(certRecord.getCheckPhoneNum(), "SMS_191833074");
    }

}
