package com.yeweihui.modules.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.user.entity.DuqinAppUserEntity;

import java.util.Map;

/**
 * APP用户
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-04-01 17:30:16
 */
public interface DuqinAppUserService extends IService<DuqinAppUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

