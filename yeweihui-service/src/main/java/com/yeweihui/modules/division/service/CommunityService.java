package com.yeweihui.modules.division.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.division.entity.CityEntity;
import com.yeweihui.modules.division.entity.CommunityEntity;

import java.util.List;
import java.util.Map;

public interface CommunityService extends IService<CommunityEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CommunityEntity> findBySubdistrictId(Long subdistrictId);

}
