package com.yeweihui.modules.user.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.InviteForm;
import com.yeweihui.modules.vo.api.form.user.*;
import com.yeweihui.modules.vo.api.vo.LoginVO;
import com.yeweihui.modules.vo.api.vo.RoleZoneInfoVO;
import com.yeweihui.modules.vo.api.vo.UserListDivideGroup;
import com.yeweihui.modules.vo.query.UserQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 业委会用户
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
public interface UserService extends IService<UserEntity> {
    /*************************************************
     *                sysUserService
     ************************************************/

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 保存用户
     */
    void save(UserEntity user);

    /**
     * 修改用户
     */
    void update(UserEntity user);

    /**
     * 更新用户头像
     * @param updateUserForm
     */
    void updateAvatarUrl(UpdateUserForm updateUserForm);

    /**
     * 修改密码
     * @param userId       用户ID
     * @param password     原密码
     * @param newPassword  新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);

    /************************************************
     *               normalUserService
     ************************************************/


    UserEntity queryByMobile(String mobile);


    /**
     * api用户登陆返回token和超时时间
     * @param form
     * @return
     */
    LoginVO login(LoginForm form);

    /**
     * 注册
     * @param registerForm
     */
    void register(RegisterForm registerForm);
    /**
     * 短信登录
     * @param smsLoginForm
     * @return
     */
    LoginVO smsLogin(SmsLoginForm smsLoginForm);

    /**
     * 修改用户密码
     * @param modifyPasswordForm
     */
    void modifyPassword(ModifyPasswordForm modifyPasswordForm);

    /**
     * api根据手机号查询用户
     * @param phone
     * @return
     */
    UserEntity getByPhone(String phone);

    /**
     * 用户列表
     * @param userQueryParam
     * @return
     */
    List<UserEntity> simpleList(@Param("userQueryParam") UserQueryParam userQueryParam);

    /**
     * 获取主任角色，获取当前小区主任用户
     * @param zoneId
     * @return
     */
    UserEntity getDirectorByZoneId(Long zoneId);

    /**
     * 根据openId获取用户
     * @param openId
     * @return
     */
    UserEntity getByOpenId(String openId);

    /**
     * 根据微信用户信息注册一个系统用户
     * @param userInfo
     * @param phone
     */
    UserEntity registerWx(WxMaUserInfo userInfo, String phone);

    /**
     * 根据openId登录
     * @param openid
     * @return
     */
    LoginVO loginByOpenId(String openid);

    /**
     * 用户根据邀请码加入小区，选择角色，输入手机号，业委会和业主要输入房间号
     * @param inviteForm
     * @return
     */
    UserEntity inviteToZones(InviteForm inviteForm);

    /**
     * 小区管理员审核用户
     * @param userId
     * @param verifyStatus
     */
    void verifyUser(Long userId, Integer verifyStatus);

    /**
     * 用户简单列表，根据group分组
     * @param userQueryParam
     * @return
     */
    List<UserListDivideGroup> simpleListDivide(UserQueryParam userQueryParam);

    /**
     * 新增返回接口，返回用户角色，小区开通日期，服务截止日期
     * @param userEntity
     * @return
     */
    RoleZoneInfoVO getRoleZoneInfo(UserEntity userEntity);

    /**
     * 微信用户首次绑定手机号，绑定用户
     * @param bindPhoneForm
     */
    void bindPhone(BindPhoneForm bindPhoneForm);

    /**
     * 根据openId和手机号登录
     * @param openid
     * @param phone
     * @return
     */
    LoginVO loginByOpenIdAndPhone(String openid, String phone);

    /**
     * 新增用户密码
     * @param id
     * @param password
     */
    void authAddPassword(Long id, String password);

    UserEntity info(Long userId);

    /**
     * 根据请求用户获取他能看的用户列表
     * @param params
     * @return
     */
    List<UserEntity> listByUser(Map<String, Object> params);

    /**
     * 根据roleName获取group
     *
     */
    String getGroupByRoleName(String roleName);
}

