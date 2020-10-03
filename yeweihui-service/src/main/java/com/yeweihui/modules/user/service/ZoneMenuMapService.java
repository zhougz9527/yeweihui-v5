package com.yeweihui.modules.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.user.entity.ZoneMenuMapEntity;

import java.util.List;

public interface ZoneMenuMapService extends IService<ZoneMenuMapEntity> {

    List<ZoneMenuMapEntity> selectListByZoneIdAndGroupId(Long zoneId, Long groupId);

    void insertByZoneId(Long zoneId);

}
