package com.yeweihui.modules.user.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.base.Verify;
import com.yeweihui.common.annotation.DataFilter;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.common.utils.StrUtils;
import com.yeweihui.common.validator.Assert;
import com.yeweihui.modules.CommonService;
import com.yeweihui.modules.enums.request.RequestStatusEnum;
import com.yeweihui.modules.jmkj.Entity.DivisionManagerBean;
import com.yeweihui.modules.jmkj.service.impl.JmkjServiceImpl;
import com.yeweihui.modules.sys.entity.SysDeptEntity;
import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.entity.SysUserRoleEntity;
import com.yeweihui.modules.sys.service.SysDeptService;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.dao.UserDao;
import com.yeweihui.modules.user.entity.TokenEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.TokenService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.api.form.InviteForm;
import com.yeweihui.modules.vo.api.form.user.*;
import com.yeweihui.modules.vo.api.vo.LoginVO;
import com.yeweihui.modules.vo.api.vo.RoleZoneInfoVO;
import com.yeweihui.modules.vo.api.vo.UserListDivideGroup;
import com.yeweihui.modules.vo.query.UserQueryParam;
import com.yeweihui.third.sms.SendSmsUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@EnableScheduling
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TokenService tokenService;

    @Autowired
    CommonService commonService;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    ZonesService zonesService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    JmkjServiceImpl mJmkjServiceImpl;

    /** key: roleName, value: group.*/
    private Map<String, String> roleNameToGroupMap;

    @PostConstruct
    private void init() {
        initGroupMap();
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void initGroupMap() {
        List<SysRoleEntity> list = sysRoleService.simpleList(null);
        Map<String, String> tmp = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (SysRoleEntity item : list) {
                tmp.put(item.getRoleName(), item.getGroup());
            }
        } else {
            tmp = Collections.emptyMap();
        }
        roleNameToGroupMap = tmp;
        logger.info("reload roleName to group map.");
    }

    @Override
    public String getGroupByRoleName(String roleName) {
        if (StringUtils.isNotBlank(roleName)) {
            return roleNameToGroupMap.get(roleName);
        }
        return Strings.EMPTY;
    }

    /*************************************************
     *                sysUserService
     ************************************************/

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    @DataFilter(subDept = true, user = false)
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        String keyword = (String)params.get("keyword");
        String zoneId = (String)params.get("zoneId");
        Long authZoneId = (Long)params.get("authZoneId");
        List<Long> authZoneIdList = (List<Long>)params.get("authZoneIdList");
        //审核状态 0审核中 1审核通过 2撤销 3未通过
        Integer userVerifyStatus = (Integer)params.get("userVerifyStatus");
        //状态 0禁用 1正常
        Integer status = (Integer)params.get("status");

        Page<UserEntity> page = this.selectPage(
                new Query<UserEntity>(params).getPage(),
                new EntityWrapper<UserEntity>()
                        .andNew().like(StringUtils.isNotBlank(keyword),"realname", keyword)
                        .or().like(StringUtils.isNotBlank(keyword),"mobile", keyword)
                        .eq(StringUtils.isNotBlank(zoneId), "zone_id", zoneId)
                        .in(null != authZoneIdList, "zone_id", authZoneIdList)
                        //authZoneId优先级高
                        .eq( null != authZoneId , "zone_id", authZoneId)
                        //审核状态
                        .eq(null != userVerifyStatus , "verify_status", userVerifyStatus)
                        //状态 0禁用 1正常
                        .eq(null != status , "status", status)
                        //没有关键词时不查行业主管
                        .ne(StringUtils.isBlank(keyword), "role_name", "行业主管")
                //暂时不需要部门，区域选项
//                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );

        for(UserEntity userEntity : page.getRecords()){
            this.pkgExtraInfo(userEntity);
        }

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserEntity user) {
        //判断手机号是否已使用
        UserEntity userEntity = this.queryByMobile(user.getMobile());
        if (null != userEntity){
            throw new RRException("当前手机号已经注册过用户");
        }

        user.setCreateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        user.setVerifyStatus(1);
        this.insert(user);

        if (null == user.getRoleIdList() || user.getRoleIdList().size() == 0){
            throw new RRException("请选择一个用户角色");
        }/*else if (null != user.getRoleIdList() || user.getRoleIdList().size()!= 1){
//            throw new RRException("当前用户角色只能单选");
        }*/else{
            //保存用户与角色关系
            sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());

            for (Long roleId : user.getRoleIdList()) {
                SysRoleEntity sysRoleEntity = sysRoleService.selectById(roleId);
                Assert.isNull(sysRoleEntity, "角色不存在，请检查后再试");
                if (roleId.equals(1L)){
                    Long zoneId = user.getZoneId();
                    List<SysUserRoleEntity> sysUserRoleList = sysUserRoleService.selectList(new EntityWrapper<SysUserRoleEntity>()
                            .eq("role_id", 1));
                    for (SysUserRoleEntity sysUserRole : sysUserRoleList) {
                        UserEntity candidate = this.selectById(sysUserRole.getUserId());
                        if (candidate.getZoneId().equals(zoneId) && !candidate.getId().equals(user.getId())) {
                            throw new RRException("该小区管理员已存在，无法设置");
                        }
                    }
                    user.setManager(true);
                } else {
                    user.setRoleId(roleId);
                    user.setRoleName(sysRoleEntity.getRoleName());
                    user.setRoleCode(sysRoleEntity.getRemark());
                }
            }

        }

        this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserEntity user) {
        //判断手机号是否已使用
        UserEntity userEntity = this.queryByMobile(user.getMobile());
        if (null != userEntity && !user.getId().equals(userEntity.getId())){
            throw new RRException("当前手机号已经注册过用户");
        }

        if(StringUtils.isBlank(user.getPassword())){
            user.setPassword(null);
        }else{
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
        }

        if (null == user.getRoleIdList() || user.getRoleIdList().size() == 0){
            throw new RRException("请选择一个用户角色");
        }/*else if (null != user.getRoleIdList() || user.getRoleIdList().size()!= 1){
//            throw new RRException("当前用户角色只能单选");
        }*/
        else{
            //保存用户与角色关系
            sysUserRoleService.saveOrUpdate(user.getId(), user.getRoleIdList());

            for (Long roleId : user.getRoleIdList()) {
                SysRoleEntity sysRoleEntity = sysRoleService.selectById(roleId);
                Assert.isNull(sysRoleEntity, "角色不存在，请检查后再试");
                if (roleId.equals(1L)){
                    Long zoneId = user.getZoneId();
                    List<SysUserRoleEntity> sysUserRoleList = sysUserRoleService.selectList(new EntityWrapper<SysUserRoleEntity>()
                        .eq("role_id", 1));
                    for (SysUserRoleEntity sysUserRole : sysUserRoleList) {
                        UserEntity candidate = this.selectById(sysUserRole.getUserId());
                        if (candidate.getZoneId().equals(zoneId) && !candidate.getId().equals(user.getId())) {
                            throw new RRException("该小区管理员已存在，无法设置");
                        }
                    }
                } else {
                    user.setRoleId(roleId);
                    user.setRoleName(sysRoleEntity.getRoleName());
                    user.setRoleCode(sysRoleEntity.getRemark());
                }
//                Long roleId = user.getRoleIdList().get(0);
            }
        }

        this.updateById(user);
    }

    /**
     * 更新用户头像
     * @param updateUserForm
     */
    @Override
    public void updateAvatarUrl(UpdateUserForm updateUserForm) {
        UserEntity userEntity = this.selectById(updateUserForm.getUserId());
        userEntity.setAvatarUrl(updateUserForm.getAvatarUrl());
        this.insertOrUpdate(userEntity);
    }


    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new EntityWrapper<UserEntity>().eq("id", userId).eq("password", password));
    }

    /************************************************
     *               normalUserService
     ************************************************/

    @Override
    public UserEntity queryByMobile(String mobile) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(mobile);
        return baseMapper.selectOne(userEntity);
    }

    /**
     * api用户登陆返回token和超时时间
     * @param form
     * @return
     */
    @Override
    public LoginVO login(LoginForm form) {
        UserEntity userEntity = this.getByPhone(form.getMobile());
        Assert.isNull(userEntity, "手机号或密码错误");

        //密码错误
        /*if(!userEntity.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))){
            throw new RRException("手机号或密码错误");
        }*/

        if(!userEntity.getPassword().equals(ShiroUtils.sha256(form.getPassword(), userEntity.getSalt()))){
            throw new RRException("手机号或密码错误");
        }


        LoginVO loginResult = this.innerLogin(form.getMobile());
        return loginResult;
    }

    /**
     * 注册
     * @param registerForm
     */
    @Override
    public void register(RegisterForm registerForm) {
        commonService.checkVerifyCode(registerForm.getMobile(), registerForm.getVerifyCode());
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(registerForm.getMobile());
        userEntity.setUsername(registerForm.getMobile());

        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        userEntity.setSalt(salt);
        userEntity.setPassword(ShiroUtils.sha256(userEntity.getPassword(), userEntity.getSalt()));
        userEntity.setNickname(StrUtil.replace(registerForm.getMobile(), 3, 7, '*'));
        this.insert(userEntity);
    }

    /**
     * 短信登录
     * @param smsLoginForm
     * @return
     */
    @Override
    public LoginVO smsLogin(SmsLoginForm smsLoginForm) {
        commonService.checkVerifyCode(smsLoginForm.getMobile(), smsLoginForm.getVerifyCode());
        LoginVO loginResult = this.innerLogin(smsLoginForm.getMobile());
        return loginResult;
    }

    /**
     * 修改用户密码
     * @param modifyPasswordForm
     */
    @Override
    public void modifyPassword(ModifyPasswordForm modifyPasswordForm) {
        commonService.checkVerifyCode(modifyPasswordForm.getMobile(), modifyPasswordForm.getVerifyCode());
        UserEntity userEntity = this.getByPhone(modifyPasswordForm.getMobile());
        if (null == userEntity){
            throw new RRException("当前手机号用户不存在");
        }
        userEntity.setPassword(ShiroUtils.sha256(modifyPasswordForm.getPassword(), userEntity.getSalt()));
        this.updateById(userEntity);
    }

    /**
     * api根据手机号查询用户
     * @param phone
     * @return
     */
    @Override
    public UserEntity getByPhone(String phone) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(phone);
        return baseMapper.selectOne(userEntity);
    }

    /**
     * 用户列表
     * @param userQueryParam
     * @return
     */
    @Override
    public List<UserEntity> simpleList(UserQueryParam userQueryParam) {
        // 角色组 用户角色类型
        if (StringUtils.isNotBlank(userQueryParam.getGroupName())){
            //获取当前角色组的角色roleCodeList
            List<String> roleCodeList = sysRoleService.getRoleCodeByGroup(userQueryParam.getGroupName());
            //根据小区id和角色列表获取用户人员列表
            userQueryParam.setRoleCodeList(roleCodeList);
        }
        return this.baseMapper.simpleList(userQueryParam);
    }

    @Override
    @DataFilter(subDept = true, user = false)
    @ZoneFilter
    public List<UserEntity> listByUser(Map<String, Object> params) {
        String zoneId = (String)params.get("zoneId");
        Long authZoneId = (Long)params.get("authZoneId");
        List<Long> authZoneIdList = (List<Long>)params.get("authZoneIdList");
        Boolean operateManager = (Boolean) params.get(Constant.OPERATE_MANAGER);
        //审核状态 0审核中 1审核通过 2撤销 3未通过
        Integer userVerifyStatus = (Integer)params.get("userVerifyStatus");
        //状态 0禁用 1正常
        Integer status = (Integer)params.get("status");

        List<UserEntity> list = null;
        if (operateManager != null && operateManager) {
            list = this.selectList(
                    new EntityWrapper<UserEntity>()
                            .andNew()
                            //审核状态
                            .eq(null != userVerifyStatus , "verify_status", userVerifyStatus)
                            //状态 0禁用 1正常
                            .eq(null != status , "status", status)
                    //暂时不需要部门，区域选项
//                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
            );
        } else {
            list = this.selectList(
                    new EntityWrapper<UserEntity>()
                            .andNew().eq(StringUtils.isNotBlank(zoneId), "zone_id", zoneId)
                            .in(null != authZoneIdList, "zone_id", authZoneIdList)
                            //authZoneId优先级高
                            .eq( null != authZoneId , "zone_id", authZoneId)
                            //审核状态
                            .eq(null != userVerifyStatus , "verify_status", userVerifyStatus)
                            //状态 0禁用 1正常
                            .eq(null != status , "status", status)
                    //暂时不需要部门，区域选项
//                        .addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
            );
        }

        for(UserEntity userEntity : list){
            this.pkgExtraInfo(userEntity);
        }
        return list;
    }

    /**
     * 获取主任角色，获取当前小区主任用户
     * @param zoneId
     * @return
     */
    @Override
    public UserEntity getDirectorByZoneId(Long zoneId) {
        return this.baseMapper.getDirectorByZoneId(zoneId);
    }

    /**
     * 根据openId获取用户
     * @param openId
     * @return
     */
    @Override
    public UserEntity getByOpenId(String openId) {
        return this.baseMapper.getByOpenId(openId);
    }

    /**
     * 根据微信用户信息注册一个系统用户
     * @param userInfo
     * @param phone
     */
    @Override
    public UserEntity registerWx(WxMaUserInfo userInfo, String phone) {
        UserEntity userEntity = this.getByOpenId(userInfo.getOpenId());
        if (null == userEntity){
            userEntity = this.getByPhone(phone);
            if (null == userEntity){
                //openid和手机号都是空的，用户没有注册过
                userEntity = new UserEntity();
                userEntity.setUsername(phone);
                userEntity.setNickname(userInfo.getNickName());
                userEntity.setOpenid(userInfo.getOpenId());
                //unionId
                userEntity.setGender(Integer.valueOf(userInfo.getGender()));
                userEntity.setCountry(userInfo.getCountry());
                userEntity.setProvince(userInfo.getProvince());
                userEntity.setCity(userInfo.getCity());
                userEntity.setAvatarUrl(userInfo.getAvatarUrl());

                userEntity.setMobile(phone);
                //sha256加密
                String salt = RandomStringUtils.randomAlphanumeric(20);
                userEntity.setSalt(salt);
                userEntity.setPassword(ShiroUtils.sha256("123456", userEntity.getSalt()));
                this.insert(userEntity);
            }else if (null != userEntity){
                //openId为空手机号不为空，根据手机号更新微信用户的信息
                userEntity.setUsername(userInfo.getNickName());
                userEntity.setNickname(userInfo.getNickName());
                userEntity.setOpenid(userInfo.getOpenId());
                //unionId
                userEntity.setGender(Integer.valueOf(userInfo.getGender()));
                userEntity.setCountry(userInfo.getCountry());
                userEntity.setProvince(userInfo.getProvince());
                userEntity.setCity(userInfo.getCity());
                userEntity.setAvatarUrl(userInfo.getAvatarUrl());

                userEntity.setMobile(phone);
                //sha256加密
                String salt = RandomStringUtils.randomAlphanumeric(20);
                userEntity.setSalt(salt);
                userEntity.setPassword(ShiroUtils.sha256("123456", userEntity.getSalt()));
                this.updateById(userEntity);
//                this.update(userEntity);
            }
        }else if(null != userEntity){
            if (StringUtils.isNotBlank(userEntity.getMobile())){
                //openId不为空手机号不为空，就不会调用这里来注册了
            }else if (StringUtils.isBlank(userEntity.getMobile())){
                //openId不为空手机号为空，更新手机号到根据openId获取过来的用户
                userEntity.setMobile(phone);
                this.update(userEntity);
            }
        }
        return userEntity;

    }

    /**
     * 根据openId登录
     * @param openid
     * @return
     */
    @Override
    public LoginVO loginByOpenId(String openid) {
        LoginVO loginResult = new LoginVO();
        UserEntity userEntity = this.getByOpenId(openid);
        //查询是否该openId已经存在，如果之前没有openId注册过的话直接返回
        if (null == userEntity){
            return loginResult;
        }
        //获取登录token
        TokenEntity tokenEntity = tokenService.createToken(userEntity.getId());

        //更新最后一次登录时间
        userEntity.setLastLogin(new Date());
        this.updateById(userEntity);

        loginResult.setToken(tokenEntity.getToken());
        loginResult.setExpire(tokenEntity.getExpireTime().getTime() - System.currentTimeMillis());

        return loginResult;
    }

    /**
     * 用户根据邀请码加入小区，选择角色，输入手机号，业委会和业主要输入房间号
     * @param inviteForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserEntity inviteToZones(InviteForm inviteForm) {
        /*//验证码校验
        String verifyCode = inviteForm.getVerifyCode();
        String phone = inviteForm.getPhone();
        commonService.checkVerifyCode(phone, verifyCode);*/

        ZonesEntity zonesEntity = zonesService.getByInviteCode(inviteForm.getInviteCode());
        Assert.isNull(zonesEntity, "当前小区未生成邀请码");
        UserEntity userEntity = this.getByOpenId(inviteForm.getOpenId());
        Assert.isNull(userEntity, "当前用户的openId未注册");

        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setZoneId(zonesEntity.getId());
        List<UserEntity> userEntityList = this.simpleList(userQueryParam);
        logger.info("inviteToZones 小区是否有用户 userEntityList:{}, userEntityList.size():{}",
                StrUtils.toJson(userEntityList), userEntityList.size());
        List<Long> roleIdList = new ArrayList<>();
        if (null == userEntityList || userEntityList.size()==0){
            //当前小区没有用户，第一个注册的为小区管理员
            userEntity.setRoleId(1L);
            userEntity.setRoleCode("1000");
            userEntity.setRoleName("小区管理员");
            roleIdList.add(1L);
        }else{
            userEntity.setRoleId(inviteForm.getRoleId());
            SysRoleEntity sysRoleEntity = sysRoleService.selectById(inviteForm.getRoleId());
            userEntity.setRoleCode(sysRoleEntity.getRemark());
            userEntity.setRoleName(sysRoleEntity.getRoleName());
            roleIdList.add(inviteForm.getRoleId());
        }
        //保存用户与角色关系(fixme：为了给后台填值使用)
        sysUserRoleService.saveOrUpdate(userEntity.getId(), roleIdList);

        userEntity.setZoneId(zonesEntity.getId());
        userEntity.setZoneName(zonesEntity.getName());
//        userEntity.setOpenid(inviteForm.getOpenId());
//        userEntity.setMobile(inviteForm.getPhone());
        if (userEntity.getMobile().equals("13396560822")){
            userEntity.setVerifyStatus(RequestStatusEnum.审核通过.getCode());
        }else{
            userEntity.setVerifyStatus(RequestStatusEnum.审核中.getCode());
        }

        if (StringUtils.isNotBlank(inviteForm.getRoomNum())){
            userEntity.setRoomNum(inviteForm.getRoomNum());
        }
        userEntity.setRealname(inviteForm.getRealname());
        this.updateById(userEntity);
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(userEntity.getId(), Arrays.asList(inviteForm.getRoleId()));

        return userEntity;
    }

    /**
     * 小区管理员审核用户
     * @param userId
     * @param verifyStatus
     */
    @Override
    public void verifyUser(Long userId, Integer verifyStatus) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setVerifyStatus(verifyStatus);
        this.updateById(userEntity);

        if (verifyStatus==1){

            new SendSmsUtils().sendSmsByTem(this.selectById(userId).getMobile(), "SMS_204761640", "{\"type\":\""+ "成功" +"\"}");

        }else if (verifyStatus==3){

            new SendSmsUtils().sendSmsByTem(this.selectById(userId).getMobile(), "SMS_204761640", "{\"type\":\""+ "失败" +"\"}");
        }
    }

    @Override
    public List<UserListDivideGroup> simpleListDivide(Long uid,UserQueryParam userQueryParam) {
        List<UserListDivideGroup> userListDivideGroupList = new ArrayList<>();
        List<UserEntity> userEntityList;
        UserListDivideGroup userListDivideGroup;
        if (StringUtils.isNotBlank(userQueryParam.getGroupName())){
            userListDivideGroup = new UserListDivideGroup();
            userListDivideGroup.setGroupName(userQueryParam.getGroupName());

            userEntityList = this.simpleList(userQueryParam);
            userListDivideGroup.setUserEntityList(userEntityList);
            userListDivideGroupList.add(userListDivideGroup);
        }else{
            userListDivideGroup = new UserListDivideGroup();
            userQueryParam.setGroupName("业主委员会");
            userListDivideGroup.setGroupName(userQueryParam.getGroupName());
            userEntityList = this.simpleList(userQueryParam);
            userListDivideGroup.setUserEntityList(userEntityList);
            userListDivideGroupList.add(userListDivideGroup);

            userListDivideGroup = new UserListDivideGroup();
            userQueryParam.setGroupName("物业公司");
            userListDivideGroup.setGroupName(userQueryParam.getGroupName());
            userEntityList = this.simpleList(userQueryParam);
            userListDivideGroup.setUserEntityList(userEntityList);
            userListDivideGroupList.add(userListDivideGroup);

            userListDivideGroup = new UserListDivideGroup();
            userQueryParam.setGroupName("小区业主");
            userListDivideGroup.setGroupName(userQueryParam.getGroupName());
            userEntityList = this.simpleList(userQueryParam);
            userListDivideGroup.setUserEntityList(userEntityList);
            userListDivideGroupList.add(userListDivideGroup);

            List<DivisionManagerBean> mDivisionManagerBeanList = (List<DivisionManagerBean>)mJmkjServiceImpl.administratorList(uid);

            userListDivideGroup = new UserListDivideGroup();
            userQueryParam.setGroupName("行业经理");
            userListDivideGroup.setGroupName(userQueryParam.getGroupName());
            userEntityList = new ArrayList<>();
            for (DivisionManagerBean datas:mDivisionManagerBeanList){

                UserEntity mUserEntity = this.selectById(datas.getId());
                mUserEntity.setRealname(mJmkjServiceImpl.getNmsl("division_"+datas.getLevel(),datas.getDivisionId()));
                userEntityList.add(mUserEntity);
            }
            userListDivideGroup.setUserEntityList(userEntityList);
            userListDivideGroupList.add(userListDivideGroup);

            // 行业主管不需要展示 20191229
//            userListDivideGroup = new UserListDivideGroup();
//            userQueryParam.setGroupName("行业主管");
//            userListDivideGroup.setGroupName(userQueryParam.getGroupName());
//            userEntityList = this.simpleList(userQueryParam);
//            userListDivideGroup.setUserEntityList(userEntityList);
//            userListDivideGroupList.add(userListDivideGroup);

        }
        return userListDivideGroupList;
    }

    /**
     * 新增返回接口，返回用户角色，小区开通日期，服务截止日期
     * @param userEntity
     * @return
     */
    @Override
    public RoleZoneInfoVO getRoleZoneInfo(UserEntity userEntity) {
        RoleZoneInfoVO roleZoneInfoVO = new RoleZoneInfoVO();
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userEntity.getId());
        List<SysRoleEntity> sysRoleEntityList = new ArrayList<>();
        for (Long roleId : roleIdList) {
            SysRoleEntity sysRoleEntity = sysRoleService.selectById(roleId);
            sysRoleEntityList.add(sysRoleEntity);
        }
        ZonesEntity zonesEntity = zonesService.selectById(userEntity.getZoneId());
        Assert.isNull(zonesEntity, "当前用户所在的小区不存在");
        roleZoneInfoVO.setSysRoleEntityList(sysRoleEntityList);
        roleZoneInfoVO.setEnableUseTime(zonesEntity.getEnableUseTime());
        roleZoneInfoVO.setRegisterTime(zonesEntity.getRegisterTime());
        return roleZoneInfoVO;
    }

    /**
     * 微信用户首次绑定手机号，绑定用户
     * @param bindPhoneForm
     */
    @Override
    public void bindPhone(BindPhoneForm bindPhoneForm) {
        String phone = bindPhoneForm.getMobile();
        UserEntity userEntity = this.getByPhone(phone);
        if (null == userEntity){
            userEntity = this.selectById(bindPhoneForm.getUserId());
            userEntity.setMobile(bindPhoneForm.getMobile());
            this.updateById(userEntity);
        }else if(null != userEntity){
            //说明手机号存在，需要合并两个用户
            //后台用户的角色信息，当前用户的
            // FIXME: 2019/10/23 
//            UserEntity userEntity = sysUserRoleService.queryRoleIdList(userEntity.getId());
        }

    }

    /**
     * 根据openId和手机号登录
     * @param openid
     * @param phone
     * @return
     */
    @Override
    public LoginVO loginByOpenIdAndPhone(String openid, String phone) {
        LoginVO loginResult = new LoginVO();
        UserEntity userEntity = this.getByOpenId(openid);
        //查询是否该openId已经存在，如果之前没有openId注册过的话直接返回
        if (null == userEntity){
            userEntity = this.getByPhone(phone);
            //查询是否该手机号已经存在，说明是后台注册过的用户
            if (null == userEntity){
                //openId和手机号都查询不出来，直接返回
                return loginResult;
            }
        }

        if (StringUtils.isNotBlank(userEntity.getOpenid()) && StringUtils.isNotBlank(userEntity.getMobile())){
            //获取登录token
            TokenEntity tokenEntity = tokenService.createToken(userEntity.getId());

            //更新最后一次登录时间
            userEntity.setLastLogin(new Date());
            this.updateById(userEntity);

            loginResult.setToken(tokenEntity.getToken());
            loginResult.setExpire(tokenEntity.getExpireTime().getTime() - System.currentTimeMillis());
        }

        return loginResult;
    }

    /**
     * 新增用户密码
     * @param uid
     * @param password
     */
    @Override
    public void authAddPassword(Long uid, String password) {
        UserEntity userEntity = this.selectById(uid);
        String passwordSalt = ShiroUtils.sha256(password, userEntity.getSalt());
        userEntity.setPassword(passwordSalt);
        this.updateById(userEntity);
    }

    /**
     * 用户信息
     * @param userId
     * @return
     */
    @Override
    public UserEntity info(Long userId) {
        UserEntity userEntity = this.selectById(userId);
        this.pkgExtraInfo(userEntity);
        return userEntity;
    }

    /**
     * 登陆返回登陆信息
     * @param phone
     * @return
     */
    private LoginVO innerLogin(String phone){
        UserEntity userEntity = this.getByPhone(phone);
        Assert.isNull(userEntity, "该用户不存在，请先注册");
        //获取登录token
        TokenEntity tokenEntity = tokenService.createToken(userEntity.getId());

        LoginVO loginResult = new LoginVO();
        loginResult.setToken(tokenEntity.getToken());
        loginResult.setExpire(tokenEntity.getExpireTime().getTime() - System.currentTimeMillis());
        return loginResult;
    }

    /**
     * 包装多余信息
     * @param userEntity
     */
    private void pkgExtraInfo(UserEntity userEntity){
        if (null == userEntity){
            return;
        }
        SysDeptEntity sysDeptEntity = sysDeptService.selectById(userEntity.getDeptId());
        if (null != sysDeptEntity){
            userEntity.setDeptName(sysDeptEntity.getName());
        }
        ZonesEntity zonesEntity = zonesService.selectById(userEntity.getZoneId());
        if (null != zonesEntity){
            userEntity.setZoneName(zonesEntity.getName());
            userEntity.setZoneInviteCode(zonesEntity.getInviteCode());
        }

        //获取用户所属的角色列表
        List<String> roleNameList = sysUserRoleService.queryRoleNameList(userEntity.getId());
        userEntity.setRoleNameList(roleNameList);
    }

}
