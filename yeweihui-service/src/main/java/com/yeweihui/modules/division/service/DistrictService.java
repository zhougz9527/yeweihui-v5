package com.yeweihui.modules.division.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.division.entity.DistrictEntity;

import java.util.List;
import java.util.Map;

public interface DistrictService extends IService<DistrictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DistrictEntity> findByCityId(Long cityId);
}
