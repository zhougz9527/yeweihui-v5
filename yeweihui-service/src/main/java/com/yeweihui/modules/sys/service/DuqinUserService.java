package com.yeweihui.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.sys.entity.DuqinUserEntity;

import java.util.Map;

/**
 * 用户表（dq_user） 20180911
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-10-21 22:37:17
 */
public interface DuqinUserService extends IService<DuqinUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

