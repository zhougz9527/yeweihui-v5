package com.yeweihui.controller.user;

import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.TokenService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.vo.api.form.InviteForm;
import com.yeweihui.modules.vo.api.form.user.*;
import com.yeweihui.modules.vo.api.vo.LoginVO;
import com.yeweihui.modules.vo.api.vo.RoleZoneInfoVO;
import com.yeweihui.modules.vo.api.vo.UserListDivideGroup;
import com.yeweihui.modules.vo.query.SysRoleQueryParam;
import com.yeweihui.modules.vo.query.UserQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api(tags="用户")
public class ApiUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    SysRoleService sysRoleService;


    /**********************************************************************
     *                          用户注册、登陆
     **********************************************************************/

    @PostMapping("/register")
    @ApiOperation("注册")
    public R register(@RequestBody RegisterForm registerForm){
        ValidatorUtils.validateEntity(registerForm);
        userService.register(registerForm);
        return R.ok();
    }

    @PostMapping("/login")
    @ApiOperation("密码登录")
    public R login(@RequestBody LoginForm form){
        ValidatorUtils.validateEntity(form);
        LoginVO loginResult = userService.login(form);
        return R.ok().put("loginResult", loginResult);
    }

    @PostMapping("/smsLogin")
    @ApiOperation("短信登录")
    public R smsLogin(@RequestBody SmsLoginForm smsLoginForm){
        ValidatorUtils.validateEntity(smsLoginForm);
        LoginVO loginResult = userService.smsLogin(smsLoginForm);
        return R.ok().put("loginResult", loginResult);
    }

    @Login
    @PostMapping("/logout")
    @ApiOperation("退出")
    public R logout(@ApiIgnore @RequestAttribute("userId") long userId){
        tokenService.expireToken(userId);
        return R.ok();
    }

    @PostMapping("/modifyPassword")
    @ApiOperation("修改用户密码")
    public R modifyPassword(@RequestBody ModifyPasswordForm modifyPasswordForm){
        ValidatorUtils.validateEntity(modifyPasswordForm);
        userService.modifyPassword(modifyPasswordForm);
        return R.ok();
    }

    @Login
    @PostMapping("/authAddPassword")
    @ApiOperation("新增用户密码")
    public R authAddPassword(@ApiIgnore @LoginUser UserEntity user,
                               @RequestBody AuthAddPasswordForm authAddPasswordForm){
        ValidatorUtils.validateEntity(authAddPasswordForm);
        userService.authAddPassword(user.getId(), authAddPasswordForm.getPassword());
        return R.ok();
    }

    /*@Login
    @PostMapping("/bindPhone")
    @ApiOperation("微信用户首次绑定手机号，绑定用户")
    public R bindPhone(@ApiIgnore @LoginUser UserEntity userEntity,
                            @RequestBody BindPhoneForm bindPhoneForm){
        ValidatorUtils.validateEntity(bindPhoneForm);
        userService.bindPhone(bindPhoneForm);
        return R.ok();
    }*/

    /**********************************************************************
     *                     获取用户信息，当前用户id
     **********************************************************************/

    @Login
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息", response = UserEntity.class, authorizations = {@Authorization(value = "token")})
    public R userInfo(@ApiIgnore @LoginUser UserEntity user) {
        return R.ok().put("user", user);
    }

    @Login
    @GetMapping("/userId")
    @ApiOperation(value = "获取当前用户ID")
    public R userInfo(@ApiIgnore @RequestAttribute("userId") Integer userId) {
        return R.ok().put("userId", userId);
    }

    /**********************************************************************
     *                              其他
     **********************************************************************/

    //用户根据邀请码加入小区，选择角色，输入手机号，业委会和业主要输入房间号
    @PostMapping("/inviteToZones")
    @ApiOperation("用户邀请进小区")
    public R inviteToZones(@RequestBody InviteForm inviteForm){
        ValidatorUtils.validateEntity(inviteForm);
        UserEntity userEntity = userService.inviteToZones(inviteForm);
        return R.ok().put("userEntity", userEntity);
    }

    @PostMapping("/simpleList")
    @ApiOperation("用户简单列表")
    public R simpleList(@RequestBody UserQueryParam userQueryParam){
        List<UserEntity> userEntityList = userService.simpleList(userQueryParam);
        return R.ok().put("userEntityList", userEntityList);
    }

    @PostMapping("/simpleListDivide")
    @ApiOperation("用户简单列表，根据group分组")
    public R simpleListDivide(@ApiIgnore @LoginUser UserEntity user,@RequestBody UserQueryParam userQueryParam){
        List<UserListDivideGroup> userListDivideGroupList = userService.simpleListDivide(user.getId(),userQueryParam);
        return R.ok().put("userListDivideGroupList", userListDivideGroupList);
    }

    //新增返回接口，返回用户角色，小区开通日期，服务截止日期
    @Login
    @PostMapping("/getRoleZoneInfo")
    @ApiOperation("新增返回接口，返回用户角色，小区开通日期，服务截止日期")
    public R getRoleZoneInfo(@ApiIgnore @LoginUser UserEntity userEntity){
        RoleZoneInfoVO getRoleZoneInfo = userService.getRoleZoneInfo(userEntity);
        return R.ok().put("getRoleZoneInfo", getRoleZoneInfo);
    }

    @Login
    @PostMapping("/sysRoleList")
    @ApiOperation("用户角色列表返回")
    public R sysRoleList(@RequestBody SysRoleQueryParam sysRoleQueryParam){
        List<SysRoleEntity> sysRoleEntityList = sysRoleService.simpleList(sysRoleQueryParam);
        return R.ok().put("sysRoleEntityList", sysRoleEntityList);
    }

    @Login
    @PostMapping("/updateAvatar")
    @ApiOperation("更新用户头像")
    public R updateAvatar(@ApiIgnore @LoginUser UserEntity userEntity,
                          @RequestBody UpdateUserForm updateUserForm) {
        ValidatorUtils.validateEntity(updateUserForm);
        updateUserForm.setUserId(userEntity.getId());
        userService.updateAvatarUrl(updateUserForm);
        return R.ok();
    }



}
