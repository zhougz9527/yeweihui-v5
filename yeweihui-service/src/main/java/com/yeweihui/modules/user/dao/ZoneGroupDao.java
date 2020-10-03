package com.yeweihui.modules.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.user.entity.ZoneGroupEntity;

public interface ZoneGroupDao extends BaseMapper<ZoneGroupEntity> {

    ZoneGroupEntity selectZoneGroupById(Long groupId);

}
