package com.yeweihui.modules.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.user.entity.UserRoleEntity;

import java.util.Map;

/**
 * 用户角色表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
public interface UserRoleService extends IService<UserRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

