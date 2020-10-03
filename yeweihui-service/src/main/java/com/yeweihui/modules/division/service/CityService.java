package com.yeweihui.modules.division.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.division.entity.CityEntity;

import java.util.List;
import java.util.Map;

public interface CityService extends IService<CityEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CityEntity> findByProvinceId(Long provinceId);
}
