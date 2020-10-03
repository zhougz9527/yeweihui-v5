package com.yeweihui.modules.bfly.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.egzosn.pay.common.util.str.StringUtils;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.CommonService;
import com.yeweihui.modules.bfly.dao.BflyUserDao;
import com.yeweihui.modules.bfly.dao.BflyUserVoteDao;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.entity.BflyUserCertRecord;
import com.yeweihui.modules.bfly.entity.BflyUserVote;
import com.yeweihui.modules.bfly.service.*;
import com.yeweihui.modules.user.dao.UserDao;
import com.yeweihui.modules.user.dao.ZonesDao;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.bfly.form.room.CheckRoomForm;
import com.yeweihui.modules.vo.bfly.form.user.SubmitCertFrom;
import com.yeweihui.modules.vo.bfly.form.user.UserForm;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.yeweihui.modules.enums.bfly.MagicEnum.*;

@Service
public class BflyUserServiceImpl extends ServiceImpl<BflyUserDao, BflyUser> implements BflyUserService {

    @Resource
    BflyRoomService bflyRoomService;
    @Resource
    ZonesService zonesService;
    @Resource
    BflyUserCertRecordService bflyUserCertRecordService;
    @Resource
    BflyTokenService bflyTokenService;
    @Resource
    ZonesDao zonesDao;
    @Resource
    BflyUserService bflyUserService;
    @Resource
    CommonService commonService;
    @Resource
    UserDao userDao;
    @Resource
    BflyVoteService bflyVoteService;
    @Resource
    BflyUserVoteDao bflyUserVoteDao;
    @Resource
    BflyUserDao bflyUserDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Query<BflyUser>(params).getPage();
        List<BflyUser> users = this.baseMapper.selectPageVO(page, params);
        page.setRecords(users);
        return new PageUtils(page);
    }

    @Override
    public String deleteUser(Long id) {
        Map<String, List<Map<String,Object>>> myVotes = bflyVoteService.myVotes(id);
        if (myVotes.get("voting") != null && myVotes.get("voting").size() != 0) {
            return "撤销认证前请先完成已参会投票";
        }
        List<Map<String, Object>> todoMeetVotes = myVotes.get("todoMeetVotes");
        todoMeetVotes.forEach(vote -> {
            BflyUserVote bflyUserVote = new BflyUserVote();
            bflyUserVote.setId(((BigInteger) vote.get("user_vote_id")).longValue());
            bflyUserVote.setStatus((Integer) 用户表决无效.getCode());
            bflyUserVoteDao.updateById(bflyUserVote);
        });
        BflyUser bflyUser = this.baseMapper.selectById(id);
        bflyUser.setIsValid((Integer) 无效.getCode());
        bflyUser.setStatus((Integer) 未审核.getCode());
        this.baseMapper.updateById(bflyUser);
        BflyUserCertRecord bflyUserCertRecord = new BflyUserCertRecord();
        bflyUserCertRecord.setStatus((Integer) 已撤销.getCode());
        bflyUserCertRecordService.update(bflyUserCertRecord,
                new EntityWrapper<BflyUserCertRecord>()
                .eq("bfly_user_id", bflyUser.getId())
        );
        return "撤销成功成功";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BflyUser updateUser(BflyUser bflyUser) {
        BflyUser oldUser = this.baseMapper.selectById(bflyUser.getId());
        oldUser.setUsername(bflyUser.getUsername());
        oldUser.setPhoneNum(bflyUser.getPhoneNum());
        oldUser.setIdCard(bflyUser.getIdCard());
        oldUser.setHousingArea(bflyUser.getHousingArea());
        this.baseMapper.updateById(oldUser);

        //同步修改用户对应的业主导入信息
        BflyRoom bflyRoom = bflyRoomService.selectById(oldUser.getBflyRoomId());
        bflyRoom.setUserName(bflyUser.getUsername());
        bflyRoom.setPhoneNum(bflyUser.getPhoneNum());
        bflyRoom.setIdCard(bflyUser.getIdCard());
        bflyRoom.setHousingArea(bflyUser.getHousingArea());
        bflyRoomService.updateById(bflyRoom);

        //同步修改同一房屋下，其他业主导入信息的的房屋面积
        BflyRoom updateArea = new BflyRoom();
        updateArea.setHousingArea(bflyUser.getHousingArea());
        bflyRoomService.update(updateArea,
                new EntityWrapper<BflyRoom>()
                        .eq("zone_id", bflyRoom.getZoneId())
                        .eq("court", bflyRoom.getCourt())
                        .eq("building", bflyRoom.getBuilding())
                        .eq("unit_name", bflyRoom.getUnitName())
                        .eq("floor_name", bflyRoom.getFloorName())
        );

        return oldUser;
    }

    /**
     * 修改认证
     * @param userForm
     */
    @Override
    public void updateUserCert(UserForm userForm) throws Exception {
        // TODO 修改认证接口需要重新
//        BflyUser bflyUser = this.selectOne(new EntityWrapper<BflyUser>().eq("bfly_room_id", userForm.getBflyRoomId()));
//        if (bflyUser.getStatus() == 0) {
//            BflyRoom bflyRoom = bflyRoomService.selectById(bflyUser.getBflyRoomId());
//            bflyRoom.setModifyHousingArea(userForm.getModifyHousingArea());
//            bflyRoom.setModifyUserName(userForm.getModifyUserName());
//            bflyRoom.setModifyIdCard(userForm.getModifyIdCard());
//            bflyRoom.setModifyPhoneNum(userForm.getModifyPhoneNum());
//            bflyRoomService.updateRoom(bflyRoom);
//        } else {
//            throw new RRException("当前用户状态不支持修改房屋信息");
//        }
    }

    /**
     * 提交认证
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String submitCert(SubmitCertFrom submitCertFrom) {
        // 认证的对应的房屋
        BflyRoom checkRoom = null;
        //校验是否可以自动审核
        Boolean pass = submitCertFrom.getPass();
        if (!pass) {
            // TODO 提交认证代码
            //校验自动审核信息是否通过
            checkRoom = bflyRoomService.selectOne(new EntityWrapper<BflyRoom>()
                    .eq("zone_id", submitCertFrom.getZoneId())
                    .eq("court", submitCertFrom.getCourt())
                    .eq("building", submitCertFrom.getBuilding())
                    .eq("unit_name", submitCertFrom.getUnitName())
                    .eq("floor_name", submitCertFrom.getFloorName())
                    .eq("user_name", submitCertFrom.getCheckUserName())
                    .eq("phone_num", submitCertFrom.getCheckPhoneNum())
                    .eq("id_card", submitCertFrom.getCheckIdCard()));
            if (null == checkRoom) {
                return "与系统资料对比有误";
            }
            //校验房屋是否存在
            List<BflyRoom> bflyRooms = bflyRoomService.selectList(new EntityWrapper<BflyRoom>()
                    .eq("zone_id", submitCertFrom.getZoneId())
                    .eq("court", submitCertFrom.getCourt())
                    .eq("building", submitCertFrom.getBuilding())
                    .eq("unit_name", submitCertFrom.getUnitName())
                    .eq("floor_name", submitCertFrom.getFloorName()));
            if (null == bflyRooms || bflyRooms.size() == 0) {
//            throw new RRException("房屋数据错误");
                return "房屋数据错误";
            }
            //校验房屋是否已被认证
            for (BflyRoom bflyRoom : bflyRooms) {
                List<BflyUser> bflyUsers = bflyUserDao.selectList(
                        new EntityWrapper<BflyUser>()
                                .eq("bfly_room_id", bflyRoom.getId())
                                .eq("status", 1)
                );
                if (null != bflyUsers && bflyUsers.size() != 0) {
                    return "信息错误，请核对信息后重新输入";
                }
            }
            if (submitCertFrom.getCheckHeaderUrl() == null || submitCertFrom.getCheckHeaderUrl().equals("")) {
                return "校验通过";
            }
        }

        //开始提交审核
        BflyUserCertRecord bflyUserCertRecord = new BflyUserCertRecord();
        bflyUserCertRecord.setBflyUserId(submitCertFrom.getBflyUserId());
        bflyUserCertRecord.setCourt(submitCertFrom.getCourt());
        bflyUserCertRecord.setBuilding(submitCertFrom.getBuilding());
        bflyUserCertRecord.setUnitName(submitCertFrom.getUnitName());
        bflyUserCertRecord.setFloorName(submitCertFrom.getFloorName());
        bflyUserCertRecord.setCheckHeaderUrl(submitCertFrom.getCheckHeaderUrl());
        String checkHouseCertificateUrl1 = null;
        String checkHouseCertificateUrl2 = null;
        String checkHouseCertificateUrls = submitCertFrom.getCheckHouseCertificateUrl();
        if (StringUtils.isNotBlank(checkHouseCertificateUrls)) {
            String[] checkHouseCertificateUrlSplit= checkHouseCertificateUrls.split(",");
            switch (checkHouseCertificateUrlSplit.length) {
                case 2:
                    checkHouseCertificateUrl2 = checkHouseCertificateUrlSplit[1];
                case 1:
                    checkHouseCertificateUrl1 = checkHouseCertificateUrlSplit[0];
                default:
            }
        }
        bflyUserCertRecord.setCheckHouseCertificateUrl(checkHouseCertificateUrl1);
        bflyUserCertRecord.setCheckHouseCertificateUrlExtra1(checkHouseCertificateUrl2);
        bflyUserCertRecord.setCheckIdCard(submitCertFrom.getCheckIdCard());
        
        String checkIdCardUrl1 = null;
        String checkIdCardUrl2 = null;
        String checkIdCardUrls = submitCertFrom.getCheckIdCardUrl();
        if (null != checkIdCardUrls) {
            String[] checkIdCardUrlSplit= checkIdCardUrls.split(",");
            switch (checkIdCardUrlSplit.length) {
                case 2:
                    checkIdCardUrl1 = checkIdCardUrlSplit[1];
                case 1:
                    checkIdCardUrl2 = checkIdCardUrlSplit[0];
                default:
            }
        }
        bflyUserCertRecord.setCheckIdCardUrl(checkIdCardUrl1);
        bflyUserCertRecord.setCheckIdCardUrlExtra1(checkIdCardUrl2);
        bflyUserCertRecord.setCheckPhoneNum(submitCertFrom.getCheckPhoneNum());
        bflyUserCertRecord.setCheckUserName(submitCertFrom.getCheckUserName());
        bflyUserCertRecord.setCheckHousingArea(submitCertFrom.getCheckHousingArea());
        BflyUser bflyUser = this.baseMapper.selectById(submitCertFrom.getBflyUserId());
        //自动审核
        if (!pass) {
            // 更新用户信息
            bflyUser.setZoneId(submitCertFrom.getZoneId());
            bflyUser.setPhoneNum(submitCertFrom.getCheckPhoneNum());
            bflyUser.setIdCard(submitCertFrom.getCheckIdCard());
            bflyUser.setUsername(submitCertFrom.getCheckUserName());
            bflyUser.setHousingArea(submitCertFrom.getCheckHousingArea());
            bflyUser.setAddress(submitCertFrom.getZoneName() + submitCertFrom.getCourt() + submitCertFrom.getBuilding() + submitCertFrom.getUnitName() + submitCertFrom.getFloorName());
            bflyUser.setIdCardUrl(checkIdCardUrl1);
            bflyUser.setIdCardUrlExtra1(checkIdCardUrl2);
            bflyUser.setHouseCertificateUrl(checkHouseCertificateUrl1);
            bflyUser.setHouseCertificateUrlExtra1(checkHouseCertificateUrl2);
            bflyUser.setHeaderUrl(submitCertFrom.getCheckHeaderUrl());
            bflyUser.setLastCertTime(new Timestamp(System.currentTimeMillis()));
            bflyUser.setBflyRoomId(checkRoom.getId());
            bflyUser.setHousingArea(checkRoom.getHousingArea());
            bflyUser.setStatus((Integer) 审核通过.getCode());
            bflyUser.setIsValid((Integer) 有效.getCode());
            this.insertOrUpdate(bflyUser);
            //更新回执
            BflyUserCertRecord record = bflyUserCertRecordService.selectOne(new EntityWrapper<BflyUserCertRecord>().eq("bfly_user_id", submitCertFrom.getBflyUserId()).eq("bfly_room_id", checkRoom.getId()));
            if (null != record) {
                bflyUserCertRecordService.deleteById(record.getId());
            }
            bflyUserCertRecord.setBflyRoomId(checkRoom.getId());
            bflyUserCertRecord.setStatus((Integer) 房屋记录审核通过.getCode());
            bflyUserCertRecord.setSubmitMethod((Integer) 自动审核.getCode());
            bflyUserCertRecordService.insertOrUpdate(bflyUserCertRecord);
            //同步更新同一房屋下，业主导入信息中的的房屋面积
            BflyRoom updateArea = new BflyRoom();
            updateArea.setHousingArea(bflyUser.getHousingArea());
            bflyRoomService.update(updateArea,
                    new EntityWrapper<BflyRoom>()
                            .eq("zone_id", bflyUser.getZoneId())
                            .eq("court", bflyUserCertRecord.getCourt())
                            .eq("building", bflyUserCertRecord.getBuilding())
                            .eq("unit_name", bflyUserCertRecord.getUnitName())
                            .eq("floor_name", bflyUserCertRecord.getFloorName())
            );
            //同步更新业主导入的房屋信息
            BflyRoom updateUpdated_at = new BflyRoom();
            updateUpdated_at.setId(checkRoom.getId());
            updateUpdated_at.setLastCertificationTime(new Timestamp(System.currentTimeMillis()));
            bflyRoomService.updateById(updateUpdated_at);
            return 审核通过.getName();
        }
        //提交人工审核
        else {
            if (submitCertFrom.getCheckHeaderUrl() == null || submitCertFrom.getCheckHeaderUrl().equals("")) {
                return "信息错误，请核对信息后重新输入";
            }
            //校验房屋是否存在
            List<BflyRoom> bflyRooms = bflyRoomService.selectList(new EntityWrapper<BflyRoom>()
                    .eq("zone_id", submitCertFrom.getZoneId())
                    .eq("court", submitCertFrom.getCourt())
                    .eq("building", submitCertFrom.getBuilding())
                    .eq("unit_name", submitCertFrom.getUnitName())
                    .eq("floor_name", submitCertFrom.getFloorName()));
            if (null == bflyRooms || bflyRooms.size() == 0) {
//            throw new RRException("房屋数据错误");
                return "房屋数据错误";
            }
            checkRoom = bflyRooms.get(0);
            for (BflyRoom bflyRoom : bflyRooms) {
                if (bflyRoom.getUserName().equals(submitCertFrom.getCheckUserName())) {
                    checkRoom = bflyRoom;
                }
            }
            //更新回执
            BflyUserCertRecord record = bflyUserCertRecordService.selectOne(new EntityWrapper<BflyUserCertRecord>().eq("bfly_user_id", submitCertFrom.getBflyUserId()).eq("bfly_room_id", checkRoom.getId()));
            if (null != record) {
                bflyUserCertRecordService.deleteById(record.getId());
            }
            bflyUserCertRecord.setBflyRoomId(checkRoom.getId());
            bflyUserCertRecord.setSubmitMethod((Integer) 手动审核.getCode());
            bflyUserCertRecordService.insertOrUpdate(bflyUserCertRecord);
            //更新用户相关信息
            bflyUser.setBflyRoomId(checkRoom.getId());
            bflyUser.setStatus((Integer) 审核中.getCode());
            this.insertOrUpdate(bflyUser);
            //发送请求给小区管理员
            sendMsgToAdmin(submitCertFrom.getZoneId());
            return "提交成功，小区管理员会帮您审核";
        }
    }

    //给管理员发送短信
//    * * 根据模板id给指定的手机号发送短信
//    * 通知管理员进行认证模版：SMS_191818114
//    * 通知业主认证通过模版：SMS_191818116
//    * 通知业主认证失败模版：SMS_191833074
    private void sendMsgToAdmin(Long zoneId) {
        UserEntity manage = userDao.getManageByZoneId(zoneId);
        if (null == manage) {
            return;
        }
        String phoneNum = manage.getMobile();
        commonService.sendMsg(phoneNum,  "SMS_191818114");
    }

    // 撤销认证
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String revokeCert(BflyUser bflyUser) {
        Map<String, List<Map<String,Object>>> myVotes = bflyVoteService.myVotes(bflyUser.getId());
        if (myVotes.get("voting") != null && myVotes.get("voting").size() != 0) {

            return "撤销认证前请先完成已参会投票";
        }
        List<Map<String, Object>> todoMeetVotes = myVotes.get("todoMeetVotes");
        todoMeetVotes.forEach(vote -> {
            BflyUserVote bflyUserVote = new BflyUserVote();
            bflyUserVote.setId(((BigInteger) vote.get("user_vote_id")).longValue());
            bflyUserVote.setStatus((Integer) 用户表决无效.getCode());
            bflyUserVoteDao.updateById(bflyUserVote);
        });

        bflyUser.setStatus((Integer) 未审核.getCode());
        bflyUser.setIsValid((Integer) 无效.getCode());
        BflyUserCertRecord bflyUserCertRecord = bflyUserCertRecordService.selectOne(new EntityWrapper<BflyUserCertRecord>().eq("bfly_user_id", bflyUser.getId()).eq("bfly_room_id", bflyUser.getBflyRoomId()));
        bflyUserCertRecord.setStatus((Integer) 房屋记录审核未通过.getCode());
        bflyUserCertRecordService.insertOrUpdate(bflyUserCertRecord);
        this.insertOrUpdate(bflyUser);
        return "撤销认证成功";
    }

    /**
     * @Description: 分页查询业主列表（已认证和认证后撤销认证的）
     * @Param:
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/13
     */
    @Override
    public HashMap<String, Object> queryOwnerInfo(Long zoneId, String ownerName, String phone, Integer[] statusArray, Integer page, Integer size) {
        Integer current = size * (page - 1);
        List<HashMap<String, Object>> bflyRooms = this.baseMapper.queryOwnerInfo(ownerName, phone, zoneId, statusArray, current, size);
        Long totalCount = this.baseMapper.queryOwnerInfoCount(ownerName, phone, zoneId, statusArray);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", bflyRooms);
        resultMap.put("totalCount", totalCount);
        resultMap.put("totalPage", (totalCount + size - 1) / size);
        resultMap.put("pageSize", size);
        resultMap.put("currPage", page);
        return resultMap;
    }

    // 统计小区的认证面积和认证户数
    @Override
    public Map<String, Object> zoneCertAreaAndCount(Long zoneId) {
        return this.baseMapper.searchCertAreaAndCount(zoneId);
    }

    // 用户信息接口
    @Override
    public BflyUser info(Long id) {
        BflyUser bflyUser = this.baseMapper.selectById(id);
        if (null != bflyUser && null != bflyUser.getZoneId() && null != bflyUser.getBflyRoomId()) {
            BflyRoom bflyRoom = bflyRoomService.selectById(bflyUser.getBflyRoomId());
            ZonesEntity zonesEntity = zonesService.selectById(bflyUser.getZoneId());
            if (null == bflyRoom || null == zonesEntity) {
                throw new RRException("用户的小区、房屋数据有误");
            }
            bflyUser.setCourt(bflyRoom.getCourt());
            bflyUser.setBuilding(bflyRoom.getBuilding());
            bflyUser.setUnitName(bflyRoom.getUnitName());
            bflyUser.setFloorName(bflyRoom.getFloorName());
            bflyUser.setZoneName(zonesEntity.getName());
        }
        return bflyUser;
    }

    @Override
    public PageUtils queryZones(Map<String, Object> params) {
        Page page = new Query(params).getPage();
        List<ZonesEntity> list = zonesDao.selectPageEn(page, params);
        list.forEach(zonesEntity -> {
            Map<String, Object> pa = new HashMap<>();
            pa.put("zoneId", zonesEntity.getId());
            List<Map<String, Long>> zoneUserList = bflyRoomService.stateRoomNumByZoneId(pa);
            List<Map<String, Object>> allAreaList = bflyRoomService.stateRoomAreaByZoneId(pa);
            Integer total = zoneUserList.size();// 小区总户数
            double allArea = 0.00;// 总面积
            for (int i = 0; i < allAreaList.size(); i++) {
                allArea += Double.parseDouble(String.valueOf(allAreaList.get(i).get("total")));
            }
            Map<String, Object> map = this.baseMapper.searchCertAreaAndCount(zonesEntity.getId());
            BigDecimal area = map.get("housingArea") == null ? new BigDecimal(0.00) : (BigDecimal) map.get("housingArea");
            zonesEntity.setCertAreaStr(area + "/" + allArea);
            zonesEntity.setCertNumStr(map.get("userCount") + "/" + total);
        });
        page.setRecords(list);
        return new PageUtils(page);
    }

    /**
     * 校验房屋的状态
     * @param checkRoomForm
     * @return false 提交房屋验证信息时需要携带身份证和房产证信息； true不需要携带身份证和房产证信息
     */
    @Override
    public boolean checkRoomStatus(CheckRoomForm checkRoomForm) {
        BflyRoom bflyRoom = new BflyRoom();
        bflyRoom.setZoneId(checkRoomForm.getZoneId());
        bflyRoom.setCourt(checkRoomForm.getCourt());
        bflyRoom.setBuilding(checkRoomForm.getBuilding());
        bflyRoom.setUnitName(checkRoomForm.getUnitName());
        bflyRoom.setFloorName(checkRoomForm.getFloorName());
        bflyRoom.setPhoneNum(checkRoomForm.getPhoneNum());
        List<BflyRoom> bflyRooms = bflyRoomService.selectList(new EntityWrapper<BflyRoom>()
                .eq("zone_id", checkRoomForm.getZoneId())
                .eq("court", checkRoomForm.getCourt())
                .eq("building", checkRoomForm.getBuilding())
                .eq("floor_name", checkRoomForm.getFloorName())
                .eq("unit_name", checkRoomForm.getUnitName())
        );
        if (null == bflyRooms || bflyRooms.size() == 0) {
            return false;
        }
        boolean checkPhoneNum = false;
        for (BflyRoom room : bflyRooms) {
            BflyUser bflyUser = bflyUserService.selectOne(new EntityWrapper<BflyUser>().eq("bfly_room_id", room.getId()));
            if (null != bflyUser && bflyUser.getIsValid() == 有效.getCode() && bflyUser.getStatus() == 审核通过.getCode()) {
                return false;
            }
            if (room.getPhoneNum().equals(checkRoomForm.getPhoneNum())) {
                checkPhoneNum = true;
            }
        }
        return checkPhoneNum;
    }

    @Override
    public Map<String, Object> queryUserNumAndAllAreaByZoneIdAndUserStatus(Long zoneId, Integer[] statusArray) {
        List<HashMap<String, Object>> ownerInfo = this.baseMapper.queryOwnerInfo(null, null, zoneId, statusArray, null, null);
        Integer userNum = ownerInfo.size();
        BigDecimal allArea = new BigDecimal(0);
        for (HashMap<String, Object> info : ownerInfo) {
            allArea = allArea.add((BigDecimal) info.get("housingArea"));
        }
//        allArea = allArea.divide(new BigDecimal(10000));
        allArea = allArea.setScale(2,BigDecimal.ROUND_HALF_UP);
        HashMap<String, Object> result = new HashMap<>();
        result.put("userNum", userNum);
        result.put("allArea", allArea);
        return result;
    }

    // 小程序注册
    @Override
    public BflyUser wxRegister(WxMaUserInfo userInfo) {
        BflyUser bflyUser = this.selectOne(new EntityWrapper<BflyUser>().eq("open_id", userInfo.getOpenId()));
        if (null != bflyUser) {
            throw new RRException("用户已存在, 不能重复注册");
        }
        BflyUser registerUser = new BflyUser();
        //后续需要过滤特殊字符
        registerUser.setNickname(userInfo.getNickName());
        registerUser.setOpenId(userInfo.getOpenId());
        registerUser.setUnionId(userInfo.getUnionId());
        registerUser.setAvatar(userInfo.getAvatarUrl());
        registerUser.setWechatUserJson(userInfo.toString());
        this.insertOrUpdate(registerUser);
        return registerUser;
    }

    /**
     * 用户登出
     * @param bflyUser
     */
    @Override
    public void logout(BflyUser bflyUser) {
        bflyTokenService.expireToken(bflyUser.getId());
    }

}
