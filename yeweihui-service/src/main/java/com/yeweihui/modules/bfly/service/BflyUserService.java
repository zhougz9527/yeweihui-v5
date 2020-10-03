package com.yeweihui.modules.bfly.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.vo.bfly.form.room.CheckRoomForm;
import com.yeweihui.modules.vo.bfly.form.user.SubmitCertFrom;
import com.yeweihui.modules.vo.bfly.form.user.UserForm;
import com.yeweihui.modules.vo.bfly.form.user.WxLoginForm;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyUserService extends IService<BflyUser> {

    PageUtils queryPage(Map<String, Object> params);

    String deleteUser(Long id);

    // 更新用户
    BflyUser updateUser(BflyUser bflyUser);

    // 更新认证
    void updateUserCert(UserForm userForm) throws Exception;

    // 提交认证
    String submitCert(SubmitCertFrom submitCertFrom) throws Exception;

    // 撤销认证
    String revokeCert(BflyUser bflyUser);

    //后台查询业主列表
    HashMap queryOwnerInfo(Long zoneId, String ownerName, String phoen, Integer[] statusArray, Integer page, Integer size);

    Map<String, Object> zoneCertAreaAndCount(Long zoneId);

    // 用户信息
    BflyUser info(Long id);

    // 小程序注册
    BflyUser wxRegister(WxMaUserInfo userInfo);

    // 用户登出
    void logout(BflyUser bflyUser);

    // 小区列表
    PageUtils queryZones(Map<String, Object> params);

    // 校验房屋的状态
    boolean checkRoomStatus(CheckRoomForm checkRoomForm);


    /**
     * 某小区某状态的用户数量和用户总房屋面积
     * @param zoneId
     * @param statusArray(userNum,allArea)
     * @return
     */
    Map<String, Object> queryUserNumAndAllAreaByZoneIdAndUserStatus(Long zoneId, Integer[] statusArray);

}
