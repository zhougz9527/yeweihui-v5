package com.yeweihui.modules.division.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.division.entity.SubdistrictEntity;

import java.util.List;
import java.util.Map;

public interface SubdistrictService extends IService<SubdistrictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SubdistrictEntity> findByDistrictId(Long districtId);
}
