package com.yeweihui.modules.common.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.common.entity.ZoneRegisterEntity;

import java.util.Map;

/**
 * 小区登记
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-10-19 17:14:58
 */
public interface ZoneRegisterService extends IService<ZoneRegisterEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(ZoneRegisterEntity zoneRegister);
}

