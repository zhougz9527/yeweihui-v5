package com.yeweihui.modules.division.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.division.entity.DivisionManagerEntity;

import java.util.Map;

public interface DivisionManagerService extends IService<DivisionManagerEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
