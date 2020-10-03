package com.yeweihui.modules.division.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.division.entity.ProvinceEntity;

import java.util.Map;

public interface ProvinceService extends IService<ProvinceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}
